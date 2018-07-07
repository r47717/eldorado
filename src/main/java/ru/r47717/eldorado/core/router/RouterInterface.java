package ru.r47717.eldorado.core.router;

import java.util.Map;
import java.util.function.Function;

public interface RouterInterface {
    RouterEntry retrieve(String route);

    void get(String name, String route, Class controller, String fn);

    void get(String name, String route, Function<String, String> closure);

    void post(String name, String route, Class controller, String fn);

    void put(String name, String route, Class controller, String fn);

    void delete(String name, String route, Class controller, String fn);

    void method(String name, String method, String pattern, Class controller, String fn);

    void method(String name, String method, String pattern, Function<String, String> closure);

    Map<String, RouterEntry> getRoutes();
}
