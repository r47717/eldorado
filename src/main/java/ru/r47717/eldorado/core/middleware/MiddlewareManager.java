package ru.r47717.eldorado.core.middleware;

import org.eclipse.jetty.server.Request;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MiddlewareManager implements MiddlewareManagerInterface
{
    private List<MiddlewareInterface> middleware = new ArrayList<>();

    @Override
    public void addMiddleware(MiddlewareInterface middleware) {
        this.middleware.add(middleware);
    }

    @Override
    public void run(Request request) {
        Iterator<MiddlewareInterface> iterator = middleware.iterator();
        while (iterator.hasNext()) {
            MiddlewareInterface middleware = iterator.next();
            if (!middleware.run(request)) {
                break;
            }
        }
    }

}
