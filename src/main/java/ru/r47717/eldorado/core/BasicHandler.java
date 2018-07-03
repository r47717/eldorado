package ru.r47717.eldorado.core;

import com.google.gson.Gson;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import app.Routes;
import ru.r47717.eldorado.core.controllers.InternalServerErrorController;
import ru.r47717.eldorado.core.controllers.PageNotFoundController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;


public class BasicHandler extends AbstractHandler {

    private final static String DEFAULT_METHOD_NAME = "index";
    private final Router router = new Router();

    private static class RequestContext {
        private Class controllerClass = null;
        private String methodName = DEFAULT_METHOD_NAME;
        private Function<String, String> closure = null;
        private List<Router.SegmentData> params = new ArrayList<>();
    }


    BasicHandler() {
        Routes.make(router);
    }


    @Override
    public void handle(String body,
                        Request baseRequest,
                        HttpServletRequest request,
                        HttpServletResponse response) throws IOException
    {
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


    private boolean getRegisteredHandler(RequestContext ctx, String body)
    {
        Router.RouterEntry entry = router.retrieve(body);

        if (entry != null) {

            if (entry.isClosure) {
               ctx.closure = entry.closure;
            } else {
                ctx.controllerClass = entry.controller;
                ctx.methodName = entry.fn;
            }

            entry.segments.entrySet().forEach(segmentDataEntry -> {
                Router.SegmentData data = segmentDataEntry.getValue();
                if (data.isParameter) {
                    ctx.params.add(data);
                }
            });

            return true;
        }

        return false; // route is not registered
    }


    private String invokeControllerMethod(RequestContext ctx) throws IllegalAccessException,
            InvocationTargetException, PageNotFoundException, InstantiationException
    {
        String output = null;
        boolean methodFound = false;

        if (ctx.closure != null) {
            String param = ctx.params.size() > 0 ? ctx.params.get(0).value : "";
            return ctx.closure.apply(param);
        }

        Object controller = ctx.controllerClass.newInstance();

        for (Method method: controller.getClass().getMethods()) {
            if (method.getName().equals(ctx.methodName)) {
                methodFound = true;

                String[] paramsArray = new String[ctx.params.size()];
                for (int i = 0; i < ctx.params.size(); i++) {
                    paramsArray[i] = ctx.params.get(i).value;
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


    private String getAppControllerPackage() {
        return "app.controllers";
    }


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
