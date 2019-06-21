package ru.r47717.eldorado.core.middleware;

import org.eclipse.jetty.server.Request;

public interface Middleware
{
    boolean run(Request request); // return false to stop the middleware pipeline
}
