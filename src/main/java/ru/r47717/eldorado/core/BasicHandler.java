package ru.r47717.eldorado.core;

import com.google.gson.Gson;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import ru.r47717.eldorado.Routes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class BasicHandler extends AbstractHandler {

    protected final static String DEFAULT_CONTROLLER_NAME = "DefaultController";
    protected final static String DEFAULT_METHOD_NAME = "index";

    private Router router = new Router();

    BasicHandler() {
        Routes.make(router);
    }

    @Override
    public void handle(String body,
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response) throws IOException
    {
        System.out.println("Body: " + body);

        String controllerName;
        String fnName;
        String controllerMethod = router.retrieve(baseRequest.getMethod().toLowerCase() + "@" + body);

        if (controllerMethod != null) {
            String[] arr = controllerMethod.split("@");
            controllerName = arr[0];
            fnName = arr[1];
        } else {
            List<String> items = getDefaultHandler(body);
            controllerName = items.get(0);
            fnName = items.get(1);
            System.out.println(controllerName + " " + fnName);
        }

        String output = "";

        try {

            Class cls = Class.forName("ru.r47717.eldorado.controllers." + controllerName);
            Object controller = cls.newInstance();

            for (Method method: controller.getClass().getMethods()) {
                if (method.getName().equals(fnName)) {
                    Map<String, String> map = (Map<String, String>) method.invoke(controller, request, response);
                    Gson gson = new Gson();
                    output = gson.toJson(map);
                    break;
                }
            }

        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | InvocationTargetException e) {
            e.printStackTrace();
        }

        PrintWriter out = response.getWriter();

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);

        out.println(output);
        baseRequest.setHandled(true);
    }


    protected List<String> getDefaultHandler(String path) {
        List<String> items = new LinkedList<>(Arrays.asList(path.split("/")));

        if (items.size() > 0 && items.get(0).isEmpty()) {
            items.remove(0);  // trim first slash
        }

        String controllerName = DEFAULT_CONTROLLER_NAME;
        String methodName = DEFAULT_METHOD_NAME;

        if (items.size() > 0) {
            String segment = items.get(0);
            controllerName = segment.substring(0, 1).toUpperCase() +
                    segment.substring(1).toLowerCase() + "Controller";
        }

        if (items.size() > 1) {
            methodName = items.get(1).toLowerCase();
        }

        return new ArrayList<>(Arrays.asList(controllerName, methodName));
    }


    protected void report404() {

    }
}
