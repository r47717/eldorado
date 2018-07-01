package ru.r47717.eldorado.core.controllers;

import java.util.HashMap;
import java.util.Map;

public class InternalErrorController extends Controller
{
    public Map<String, String> index() {
        Map<String, String> map = new HashMap<>();
        map.put("success", "false");
        map.put("message", "internal server error");

        return map;
    }
}
