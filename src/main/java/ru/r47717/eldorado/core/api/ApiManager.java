package ru.r47717.eldorado.core.api;

import ru.r47717.eldorado.core.consul.ConsulManager;
import ru.r47717.eldorado.core.env.EnvManager;
import ru.r47717.eldorado.core.router.RouterEntry;
import ru.r47717.eldorado.core.router.RouterInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ApiManager implements ApiManagerInterface {
    private List<ApiEntry> api = new ArrayList<>();

    @Override
    public void build(RouterInterface router) {
        Map<String, RouterEntry> routes = router.getRoutes();
        routes.forEach((uri, entry) -> {
            ApiEntry item = new ApiEntry();
            item.setMethod(entry.getMethod());
            item.setUri(entry.getPattern());
            item.setName(entry.getName());

            api.add(item);
        });
    }


    @Override
    public List<ApiEntry> getApi() {
        return api;
    }


    @Override
    public void registerMyself() {
        String path = "/services/" + EnvManager.getServiceName() + "/";
        api.forEach(item -> {
            String name = item.getName();
            String uri = item.getUri();
            ConsulManager.writeToConsul(path + "api/" + name, uri);
        });
    }
}
