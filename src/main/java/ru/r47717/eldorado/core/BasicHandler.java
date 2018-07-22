package ru.r47717.eldorado.core;

import app.config.MiddlewareConfig;
import com.google.gson.Gson;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import ru.r47717.eldorado.core.controllers.InternalServerErrorController;
import ru.r47717.eldorado.core.controllers.PageNotFoundController;
import ru.r47717.eldorado.core.di.Container;
import ru.r47717.eldorado.core.di.Inject;
import ru.r47717.eldorado.core.exceptions.NoDefaultConstructorException;
import ru.r47717.eldorado.core.exceptions.PageNotFoundException;
import ru.r47717.eldorado.core.middleware.MiddlewareManager;
import ru.r47717.eldorado.core.middleware.MiddlewareManagerInterface;
import ru.r47717.eldorado.core.router.RouterEntry;
import ru.r47717.eldorado.core.router.RouterInterface;
import ru.r47717.eldorado.core.router.SegmentData;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;


/**
 * Handler for all incoming HTTP requests
 */
public class BasicHandler extends AbstractHandler {

    private final static String DEFAULT_METHOD_NAME = "index";
    private RouterInterface router;
    private final MiddlewareManagerInterface middlewareManager = new MiddlewareManager();
    private Map<Class, Object> controllerCache = Collections.synchronizedMap(new HashMap<>());

    private static class RequestContext {
        private Class controllerClass = null;
        private String methodName = DEFAULT_METHOD_NAME;
        private Function<String, String> closure = null;
        private List<SegmentData> params = new ArrayList<>();
    }


    BasicHandler(RouterInterface router) {
        this.router = router;
        MiddlewareConfig.config(middlewareManager);
    }


    /**
     * @param body - request URI
     * @param baseRequest - original HTTP request
     * @param request - servlet request
     * @param response - servlet response
     * @throws IOException
     */
    @Override
    public void handle(String body,
                        Request baseRequest,
                        HttpServletRequest request,
                        HttpServletResponse response) throws IOException
    {
        if (!middlewareManager.run(baseRequest)) {
            return;
        }

        RequestContext ctx = new RequestContext();

        String output;

        try {
            if (getRegisteredHandler(ctx, body) || getDefaultHandler(ctx, body)) {
                if (ctx.closure == null) {
                    response.setContentType("application/json");
                } else {
                    response.setContentType("text/plain");
                }
                response.setStatus(HttpServletResponse.SC_OK);
                output = invokeControllerMethod(ctx);
            } else {
                throw new PageNotFoundException();
            }
        } catch (PageNotFoundException e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.setContentType("text/plain");
            PageNotFoundController controller = new PageNotFoundController();
            output = controller.index();
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("text/plain");
            InternalServerErrorController controller = new InternalServerErrorController();
            output = controller.index();
        }

        PrintWriter out = response.getWriter();
        out.println(output);
        baseRequest.setHandled(true);
    }


    /**
     * @param ctx request context
     * @param body request URL
     * @return true if found
     */
    private boolean getRegisteredHandler(RequestContext ctx, String body)
    {
        RouterEntry entry = router.retrieve(body);

        if (entry != null) {

            if (entry.isClosure()) {
               ctx.closure = entry.getClosure();
            } else {
                ctx.controllerClass = entry.getController();
                ctx.methodName = entry.getFn();
            }

            entry.getSegments().entrySet().forEach(segmentDataEntry -> {
                SegmentData data = segmentDataEntry.getValue();
                if (data.getIsParameter()) {
                    ctx.params.add(data);
                }
            });

            return true;
        }

        return false; // route is not registered
    }


    /**
     * @param ctx - request context
     * @return HTTP output
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws PageNotFoundException
     * @throws InstantiationException
     */
    private String invokeControllerMethod(RequestContext ctx) throws IllegalAccessException,
            InvocationTargetException, PageNotFoundException, InstantiationException, NoDefaultConstructorException {
        String output = null;
        boolean methodFound = false;

        if (ctx.closure != null) {
            String param = ctx.params.size() > 0 ? ctx.params.get(0).getValue() : "";
            return ctx.closure.apply(param);
        }

        Object controller = controllerCache.get(ctx.controllerClass);

        if (controller == null) {
            Constructor[] constructors = ctx.controllerClass.getConstructors();
            for (Constructor constructor: constructors) {
                if (constructor.getParameterCount() == 0) {
                    controller = constructor.newInstance();
                    break;
                }
            }
            if (controller == null) {
                throw new NoDefaultConstructorException();
            }

            controllerCache.put(ctx.controllerClass, controller);
        }

        processAnnotations(ctx.controllerClass, controller);

        for (Method method: controller.getClass().getMethods()) {
            if (method.getName().equals(ctx.methodName)) {
                methodFound = true;

                String[] paramsArray = new String[ctx.params.size()];
                for (int i = 0; i < ctx.params.size(); i++) {
                    paramsArray[i] = ctx.params.get(i).getValue();
                }

                Map<String, String> map;
                map = (Map<String, String>) ((ctx.params.size() > 0)
                        ? method.invoke(controller, paramsArray)
                        : method.invoke(controller));
                Gson gson = new Gson();
                output = gson.toJson(map);
                break;
            }
        }

        if (!methodFound) {
            throw new PageNotFoundException();
        }

        return output;
    }

    /**
     * @param controllerClass - controller class to scan for inject annotations
     * @param controllerInstance - controller instance to inject dependencies to
     */
    private void processAnnotations(Class controllerClass, Object controllerInstance) {
        Field[] fields = controllerClass.getDeclaredFields();
        for (Field field: fields) {
            Annotation annotation = field.getAnnotation(Inject.class);
            if (annotation != null) {
                try {
                    Container.inject(controllerInstance, field);
                } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
                    System.out.println("Error: could not inject dependency to the controller");
                    e.printStackTrace();
                }
            }
        }
    }


    private String getAppControllerPackage() {
        return "app.controllers";
    }


    /**
     * @param ctx - request context
     * @param path - request URI
     * @return true if successful
     */
    private boolean getDefaultHandler(RequestContext ctx, String path) {
        List<String> items = new LinkedList<>(Arrays.asList(path.split("/")));

        if (items.size() > 0 && items.get(0).isEmpty()) {
            items.remove(0);  // trim first slash
        }

        String controllerName;

        if (items.size() > 0) {
            String segment = items.get(0);
            controllerName = segment.substring(0, 1).toUpperCase() +
                    segment.substring(1).toLowerCase() + "Controller";
        } else {
            return false; // no controller name
        }

        if (items.size() > 1) {
            ctx.methodName = items.get(1).toLowerCase();
        }

        try {
            ctx.controllerClass = Class.forName(getAppControllerPackage() + "." + controllerName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

}
