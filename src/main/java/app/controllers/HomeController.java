package app.controllers;

import ru.r47717.eldorado.core.controllers.Controller;

import java.util.HashMap;
import java.util.Map;

public class HomeController extends Controller {

    public Map<String, String> index() {
        Map<String, String> map = new HashMap<>();
        map.put("test", "stuff");

        return map;
    }

    public Map<String, String> index2() {
        Map<String, String> map = new HashMap<>();
        map.put("test", "stuff 2");

        return map;
    }

    public Map<String, String> getPerson() {
        Map<String, String> map = new HashMap<>();
        map.put("name", "Mike");

        return map;
    }
}
