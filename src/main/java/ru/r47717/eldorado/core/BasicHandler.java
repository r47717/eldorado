package ru.r47717.eldorado.core;

import com.google.gson.Gson;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import app.Routes;
import ru.r47717.eldorado.core.controllers.PageNotFoundController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class BasicHandler extends AbstractHandler {

    private final static String DEFAULT_METHOD_NAME = "index";

    private Router router = new Router();
    private Class controllerClass;
    private String methodName = DEFAULT_METHOD_NAME;


    BasicHandler() {
        Routes.make(router);
    }


    @Override
    public void handle(String body,
                        Request baseRequest,
                        HttpServletRequest request,
                        HttpServletResponse response) throws IOException
    {
        if (!getRegisteredHandler(body, baseRequest) && !getDefaultHandler(body)) {
            controllerClass = PageNotFoundController.class;
            methodName = "index";
            response.setContentType("text/plain");
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } else {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
        }

        String output;
        try {
            output = invokeControllerMethod();
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("text/plain");
            output = "Wrong routing config";
        }

        PrintWriter out = response.getWriter();
        out.println(output);
        baseRequest.setHandled(true);
    }


    private boolean getRegisteredHandler(String body, Request baseRequest)
    {
        String routerHash = baseRequest.getMethod().toLowerCase() + "@" + body;
        Object[] data = (Object[]) router.retrieve(routerHash);

        if (data != null) {
            this.controllerClass = (Class) data[0];
            this.methodName = (String) data[1];

            return true;
        }

        return false; // route is not registered
    }


    private String invokeControllerMethod() throws IllegalAccessException, InstantiationException, InvocationTargetException {
        String output = null;
        boolean methodFound = false;

        Object controller = controllerClass.newInstance();

        for (Method method: controller.getClass().getMethods()) {
            if (method.getName().equals(methodName)) {
                methodFound = true;
                Map<String, String> map = (Map<String, String>) method.invoke(controller);
                Gson gson = new Gson();
                output = gson.toJson(map);
                break;
            }
        }

        if (!methodFound) {
            throw new IllegalAccessException();
        }

        return output;
    }


    protected String getAppControllerPackage() {
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
