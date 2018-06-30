package app;

import ru.r47717.eldorado.core.Router;
import app.controllers.HomeController;

public class Routes
{
    public static void make(Router router) {

        // Add routes here

        router.get("/", HomeController.class, "index");
        router.get("/test2", HomeController.class, "index2");

        router.get("/people/{name}", HomeController.class, "getPerson");
    }
}
