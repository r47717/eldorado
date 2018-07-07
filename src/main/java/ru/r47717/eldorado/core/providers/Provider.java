package ru.r47717.eldorado.core.providers;


import java.util.Map;

public class Provider implements ProviderInterface {
    private String name;
    private String url;

    public Provider(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String callService(String uri, Map<String, String> params) {
        return "";
    }
}
