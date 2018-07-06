package ru.r47717.eldorado.core.consul;

import com.orbitz.consul.Consul;
import com.orbitz.consul.KeyValueClient;
import ru.r47717.eldorado.core.api.ApiManagerInterface;
import ru.r47717.eldorado.core.env.EnvManager;
import ru.r47717.eldorado.core.router.RouterInterface;

import java.util.Map;
import java.util.Optional;


public class ConsulManager
{
    public boolean consulHealthCheck() {
        // TODO
        return true;
    }

    public static String readFromConsul(String key) {
        String url = EnvManager.getConsulServer();
        if (url == null) {
            return null;
        }

        Consul consul = Consul.builder().withUrl(url).build();
        KeyValueClient kvClient = consul.keyValueClient();
        Optional<String> value = kvClient.getValueAsString(key);

        return value.orElse(null);
    }

    public static void writeToConsul(String key, String value) {
        String url = EnvManager.getConsulServer();
        if (url == null) {
            return;
        }

        Consul consul = Consul.builder().withUrl(url).build();
        KeyValueClient kvClient = consul.keyValueClient();

        kvClient.putValue(key, value);
    }

    public static void registerMyself(ApiManagerInterface apiManager) {
        String path = "/services/" + EnvManager.getServiceName() + "/";
        Map<String, String> apiMap = apiManager.getApi();
        for (Map.Entry<String, String> entry: apiMap.entrySet()) {
            String name = entry.getKey();
            String uri = entry.getValue();

            writeToConsul(path + "api/" + name, uri);
        }
    }
}
