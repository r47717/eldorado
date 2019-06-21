package ru.r47717.eldorado.core.middleware;

import org.eclipse.jetty.server.Request;

public interface MiddlewareManager {
    void addMiddleware(Middleware middleware);
    boolean run(Request request);
}
