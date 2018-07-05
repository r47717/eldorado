package app.controllers;

import ru.r47717.eldorado.core.env.EnvManager;

import java.util.HashMap;
import java.util.Map;

public class SystemController
{
    public Map<String, String> healthcheck() {
        Map<String, String> map = new HashMap<>();
        map.put("status", "OK");
        map.put("Service name", EnvManager.getServiceName());
        map.put("Service port", String.valueOf(EnvManager.getServicePort()));

        return map;
    }
}
