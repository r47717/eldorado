package ru.r47717.eldorado.core.api;

import java.util.HashMap;
import java.util.Map;

public class ApiManager implements ApiManagerInterface {
    private Map<String, String> api = new HashMap<>();

    @Override
    public void add(String name, String uri) {
        api.put(name, uri);
    }

    @Override
    public Map<String, String> getApi() {
        return api;
    }
}
