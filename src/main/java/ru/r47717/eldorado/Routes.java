package ru.r47717.eldorado;

import ru.r47717.eldorado.core.Router;


public class Routes
{
    public static void make(Router router) {

        // Add routes here

        router.get("/", "HomeController@index");
        router.get("/test2", "HomeController@index2");
    }
}
