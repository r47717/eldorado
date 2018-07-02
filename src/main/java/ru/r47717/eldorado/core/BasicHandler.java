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

    private Router router = new Router();
    private Class controllerClass;
    private String methodName;
    private Function<String, String> closure;
    private List<Router.SegmentData> params;


    BasicHandler() {
        Routes.make(router);
    }


    private void initProperties() {
        controllerClass = null;
        methodName = DEFAULT_METHOD_NAME;
        closure = null;
        params = new ArrayList<>();
    }


    @Override
    public void handle(String body,
                        Request baseRequest,
                        HttpServletRequest request,
                        HttpServletResponse response) throws IOException
    {
        initProperties();

        String output;

        try {
            if (getRegisteredHandler(body) || getDefaultHandler(body)) {
                if (closure == null) {
                    response.setContentType("application/json");
                } else {
                    response.setContentType("text/plain");
                }
                response.setStatus(HttpServletResponse.SC_OK);
                output = invokeControllerMethod();
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


    private boolean getRegisteredHandler(String body)
    {
        Router.RouterEntry entry = router.retrieve(body);

        if (entry != null) {

            if (entry.isClosure) {
               closure = entry.closure;
            } else {
                controllerClass = entry.controller;
                methodName = entry.fn;
            }

            entry.segments.entrySet().forEach(segmentDataEntry -> {
                Router.SegmentData data = segmentDataEntry.getValue();
                if (data.isParameter) {
                    params.add(data);
                }
            });

            return true;
        }

        return false; // route is not registered
    }


    private String invokeControllerMethod() throws IllegalAccessException, InstantiationException, InvocationTargetException, PageNotFoundException {
        String output = null;
        boolean methodFound = false;

        if (closure != null) {
            String param = params.size() > 0 ? params.get(0).value : "";
            return closure.apply(param);
        }

        Object controller = controllerClass.newInstance();

        for (Method method: controller.getClass().getMethods()) {
            if (method.getName().equals(methodName)) {
                methodFound = true;

                String[] paramsArray = new String[params.size()];
                for (int i = 0; i < params.size(); i++) {
                    paramsArray[i] = params.get(i).value;
                }

                Map<String, String> map;
                map = (Map<String, String>) ((params.size() > 0)
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


    private boolean getDefaultHandler(String path) {
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
            methodName = items.get(1).toLowerCase();
        }

        try {
            controllerClass = Class.forName(getAppControllerPackage() + "." + controllerName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

}
