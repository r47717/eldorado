package ru.r47717.eldorado.core.router;

import java.util.function.Function;

public interface RouterInterface {
    RouterEntry retrieve(String route);

    void get(String route, Class controller, String fn);

    void get(String route, Function<String, String> closure);

    void post(String route, Class controller, String fn);

    void put(String route, Class controller, String fn);

    void delete(String route, Class controller, String fn);

    void method(String method, String pattern, Class controller, String fn);

    void method(String method, String pattern, Function<String, String> closure);
}
