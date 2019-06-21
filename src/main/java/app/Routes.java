package app;

import app.controllers.HomeController;
import ru.r47717.eldorado.core.router.Router;

public class Routes
{
    public static void make(Router router) {

        // Add routes here

        router.get("root", "/", HomeController.class, "index");
        router.get("test2", "/test2", HomeController.class, "index2");

        router.get("people-get", "/people/{name}", HomeController.class, "getPerson");

        router.get("closure-example", "/closure/{param}", str -> "URL parameter is: " + str);
    }
}
