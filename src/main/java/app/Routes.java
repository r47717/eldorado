package app;

import app.controllers.HomeController;
import ru.r47717.eldorado.core.router.RouterInterface;

public class Routes
{
    public static void make(RouterInterface router) {

        // Add routes here

        router.get("root", "/", HomeController.class, "index");
        router.get("test2", "/test2", HomeController.class, "index2");

        router.get("people-get", "/people/{name}", HomeController.class, "getPerson");

        router.get("closure-example", "/closure/{param}", str -> "URL parameter is: " + str);
    }
}
