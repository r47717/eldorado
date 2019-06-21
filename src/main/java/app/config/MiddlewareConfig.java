package app.config;

import ru.r47717.eldorado.core.middleware.LoggerMiddleware;
import ru.r47717.eldorado.core.middleware.MiddlewareManager;

public class MiddlewareConfig {
    public static void config(MiddlewareManager manager) {
        // Add your middleware here: manager.add(new MyCustomMiddleware)

        manager.addMiddleware(new LoggerMiddleware());
    }
}
