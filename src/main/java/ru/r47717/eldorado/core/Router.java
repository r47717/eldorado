package ru.r47717.eldorado.core;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Router {

    private Map<String, String> routes = new HashMap<>();

    public String retrieve(String route) {
        return routes.get(route);
    }

    public void get(String route, String fn) {
        method("get", route, fn);
    }

    public void post(String route, String fn) {
        method("post", route, fn);
    }

    public void put(String route, String fn) {
        method("put", route, fn);
    }

    public void delete(String route, String fn) {
        method("delete", route, fn);
    }

    public void method(String method, String route, String fn) {
        String key = method + "@" + route;
        routes.put(key, fn);
    }
}
