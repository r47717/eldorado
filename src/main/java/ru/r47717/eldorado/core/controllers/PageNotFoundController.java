package ru.r47717.eldorado.core.controllers;

import java.util.HashMap;
import java.util.Map;

public class PageNotFoundController extends Controller
{
    public Map<String, String> index() {
        Map<String, String> map = new HashMap<>();
        map.put("success", "false");
        map.put("message", "page not found");

        return map;
    }
}

