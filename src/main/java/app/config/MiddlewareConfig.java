package app.config;

import ru.r47717.eldorado.core.middleware.LoggerMiddleware;
import ru.r47717.eldorado.core.middleware.MiddlewareManagerInterface;

public class MiddlewareConfig {
    public static void config(MiddlewareManagerInterface manager) {
        // Add your middleware here: manager.add(new MyCustomMiddleware)

        manager.addMiddleware(new LoggerMiddleware());
    }
}
