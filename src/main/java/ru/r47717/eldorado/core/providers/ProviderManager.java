package ru.r47717.eldorado.core.providers;

import ru.r47717.eldorado.core.consul.ConsulManager;

import java.util.ArrayList;
import java.util.List;

public class ProviderManager implements ProviderManagerInterface {

    private List<ProviderInterface> providers = new ArrayList<>();

    @Override
    public void add(String name) {
        if (name == null || name.isEmpty()) {
            return;
        }

        String url = ConsulManager.getAliveProviderUrl(name);
        Provider provider = new Provider(name, url);
        providers.add(provider);
    }
}
