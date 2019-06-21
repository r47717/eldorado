package ru.r47717.eldorado.core.providers;

import ru.r47717.eldorado.core.consul.ConsulManager;

import java.util.ArrayList;
import java.util.List;

public class SimpleProviderManager implements ProviderManager {

    private List<Provider> providers = new ArrayList<>();

    @Override
    public void add(String name) {
        if (name == null || name.isEmpty()) {
            return;
        }

        String url = ConsulManager.getAliveProviderUrl(name);
        SimpleProvider provider = new SimpleProvider(name, url);
        providers.add(provider);
    }
}
