package app.controllers;

import java.util.HashMap;
import java.util.Map;

public class SystemController
{
    public Map<String, String> healthcheck() {
        Map<String, String> map = new HashMap<>();
        map.put("response", "OK");

        return map;
    }
}
