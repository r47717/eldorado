package ru.r47717.eldorado.core.api;

import ru.r47717.eldorado.core.consul.ConsulManager;
import ru.r47717.eldorado.core.env.EnvManager;
import ru.r47717.eldorado.core.router.RouterEntry;
import ru.r47717.eldorado.core.router.Router;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SimpleApiManager implements ApiManager {
    private List<ApiEntry> api = new ArrayList<>();

    @Override
    public void build(Router router) {
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


    private String getMyHostName() {
        InetAddress addr;

        try {
            addr = InetAddress.getLocalHost();
            return "http://" + addr.getHostName() + ":" + EnvManager.getServicePort();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return "";
    }


    @Override
    public void registerMyself() {
        List<String> tags = new ArrayList<>();
        tags.add("tag1");
        tags.add("tag2");
        tags.add("tag3");
        Map<String, String> meta = new HashMap<>();
        meta.put("url", getMyHostName());
        ConsulManager.registerService(tags, meta);

        String path = "/services/" + EnvManager.getServiceName() + "/";
        api.forEach(item -> {
            String name = item.getName();
            String uri = item.getUri();
            ConsulManager.writeToConsul(path + "api/" + name, uri);
        });
    }
}
