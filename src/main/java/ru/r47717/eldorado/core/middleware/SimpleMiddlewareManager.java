package ru.r47717.eldorado.core.middleware;

import org.eclipse.jetty.server.Request;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SimpleMiddlewareManager implements MiddlewareManager
{
    private List<Middleware> middleware = new ArrayList<>();

    @Override
    public void addMiddleware(Middleware middleware) {
        this.middleware.add(middleware);
    }

    @Override
    public boolean run(Request request) {
        Iterator<Middleware> iterator = middleware.iterator();
        while (iterator.hasNext()) {
            Middleware middleware = iterator.next();
            if (!middleware.run(request)) {
                return false;
            }
        }

        return true;
    }

}
