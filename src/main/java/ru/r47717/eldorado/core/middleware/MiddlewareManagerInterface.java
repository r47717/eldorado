package ru.r47717.eldorado.core.middleware;

import org.eclipse.jetty.server.Request;

public interface MiddlewareManagerInterface {
    void addMiddleware(MiddlewareInterface middleware);
    void run(Request request);
}
