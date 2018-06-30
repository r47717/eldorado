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

    class RequestParam {
        String name;
        int position;
        String value;
    }

    private final static String DEFAULT_CONTROLLER_NAME = "DefaultController";
    private final static String DEFAULT_METHOD_NAME = "index";

    private Router router = new Router();
    private List<RequestParam> params = new ArrayList<>();

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

        Class controllerClass = PageNotFoundController.class;
        String fnName;
        Object[] data = (Object[]) router.retrieve(baseRequest.getMethod().toLowerCase() + "@" + body);

        if (data != null) {
            controllerClass = (Class) data[0];
            fnName = (String) data[1];
            //params = getRequestParams(body);
        } else {
            List<String> items = getDefaultHandler(body);
            String controllerName = items.get(0);
            try {
                controllerClass = Class.forName("app.controllers." + controllerName);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            fnName = items.get(1);
        }

        String output = "";

        try {

            Object controller = controllerClass.newInstance();

            for (Method method: controller.getClass().getMethods()) {
                if (method.getName().equals(fnName)) {
                    Map<String, String> map = (Map<String, String>) method.invoke(controller);
                    Gson gson = new Gson();
                    output = gson.toJson(map);
                    break;
                }
            }

        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        PrintWriter out = response.getWriter();

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);

        out.println(output);
        baseRequest.setHandled(true);
    }

    private List<RequestParam> getRequestParams(String pattern, String body) {
        List<RequestParam> output = new ArrayList<>();
        List<String> patternItems = new LinkedList<>(Arrays.asList(pattern.split("/")));
        List<String> items = new LinkedList<>(Arrays.asList(body.split("/")));

        if (patternItems.size() > 0 && patternItems.get(0).isEmpty()) {
            patternItems.remove(0);  // trim first slash
        }

        if (items.size() > 0 && items.get(0).isEmpty()) {
            items.remove(0);  // trim first slash
        }

        for (int i = 0; i < items.size(); i++) {
            String segment = patternItems.get(i).trim();
            if (segment.startsWith("{") && segment.endsWith("}")) {
                RequestParam param = new RequestParam();
                param.name = segment.substring(1, segment.length() - 1).trim();
                param.position = i;
                param.value = items.get(i).trim();
                output.add(param);
            }
        }

        return output;
    }


    private List<String> getDefaultHandler(String path) {
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
