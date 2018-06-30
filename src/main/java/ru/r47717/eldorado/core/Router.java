package ru.r47717.eldorado.core;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Router {

    private Map<String, Object> routes = new HashMap<>();

    public Object retrieve(String route) {
        return routes.get(route);
    }

    public void get(String route, Class controller, String method) {
        method("get", route, controller, method);
    }

    public void post(String route, Class controller, String method) {
        method("post", route, controller, method);
    }

    public void put(String route, Class controller, String method) {
        method("put", route, controller, method);
    }

    public void delete(String route, Class controller, String method) {
        method("delete", route, controller, method);
    }

    public void method(String method, String route, Class controller, String fn) {
        String key = fn + "@" + route;
        Object[] data = new Object[]{controller, fn};
        routes.put(key, data);
    }
}
