package ru.r47717.eldorado.core.api;

import java.util.Map;

public interface ApiManagerInterface {
    void add(String name, String url);
    Map<String, String> getApi();
}
