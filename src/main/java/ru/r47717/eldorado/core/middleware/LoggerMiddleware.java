package ru.r47717.eldorado.core.middleware;

import org.eclipse.jetty.server.Request;

public class LoggerMiddleware implements MiddlewareInterface
{
    @Override
    public boolean run(Request request) {
        System.out.println("Request:");
        System.out.println("URL: " + request.getOriginalURI());
        System.out.println("Method: " + request.getMethod());
        System.out.println("Content type: " + request.getContentType());
        System.out.println("Parameters: " + request.getParameterMap().toString());

        return true;
    }
}
