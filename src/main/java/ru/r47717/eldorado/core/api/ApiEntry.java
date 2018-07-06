package ru.r47717.eldorado.core.api;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;


@Setter
@Getter
public class ApiEntry {
    private String name;
    private String method;
    private String uri;
    private Map<String, String> params = new HashMap<>();
}
