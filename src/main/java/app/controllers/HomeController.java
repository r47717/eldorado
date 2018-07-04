package app.controllers;

import app.services.MyService;
import app.services.MyService2;
import ru.r47717.eldorado.core.controllers.Controller;
import ru.r47717.eldorado.core.di.Inject;

import java.util.HashMap;
import java.util.Map;

public class HomeController extends Controller {

    @Inject
    private MyService myService;


    @Inject
    private MyService2 myService2;


    public Map<String, String> index() {
        Map<String, String> map = new HashMap<>();
        map.put("echoService", myService.echoService("my injected text 1"));

        return map;
    }

    public Map<String, String> index2() {
        Map<String, String> map = new HashMap<>();
        map.put("test", "stuff 2");

        return map;
    }

    public Map<String, String> getPerson(String name) {
        Map<String, String> map = new HashMap<>();
        map.put("greeting", myService2.concatService("Hello, ", name));

        return map;
    }
}
