package ru.r47717.eldorado.core.providers;

import java.util.Map;

public interface ProviderInterface {
    String callService(String uri, Map<String, String> params);
}
