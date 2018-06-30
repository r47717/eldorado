package ru.r47717.eldorado.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class HomeController {

    public Map<String, String> index(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> map = new HashMap<>();
        map.put("test", "stuff");

        return map;
    }

    public Map<String, String> index2(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> map = new HashMap<>();
        map.put("test", "stuff 2");

        return map;
    }
}
