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
import java.util.Map;

public class BasicHandler extends AbstractHandler {

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

        String key = router.retrieve(baseRequest.getMethod().toLowerCase() + "@" + body);
        String[] arr = key.split("@");
        String controllerName = arr[0];
        String fnName = arr[1];
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
}
