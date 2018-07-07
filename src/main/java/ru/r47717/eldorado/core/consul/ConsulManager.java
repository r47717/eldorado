package ru.r47717.eldorado.core.consul;

import com.orbitz.consul.AgentClient;
import com.orbitz.consul.Consul;
import com.orbitz.consul.KeyValueClient;
import com.orbitz.consul.NotRegisteredException;
import ru.r47717.eldorado.core.api.ApiEntry;
import ru.r47717.eldorado.core.api.ApiManagerInterface;
import ru.r47717.eldorado.core.env.EnvManager;
import java.util.List;
import java.util.Optional;


public class ConsulManager
{
    private static Runnable passThread;

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

    public static void registerService() {
        Consul consul = Consul.builder().build(); // connect to Consul on localhost
        AgentClient agentClient = consul.agentClient();

        String serviceName = EnvManager.getServiceName();
        final String serviceId = serviceName;

        agentClient.register(EnvManager.getServicePort(), 3L, serviceName, serviceId); // registers with a TTL of 3 seconds

        passThread = new Thread(() -> {
            try {
                while(true) {
                    System.out.println("pass");
                    agentClient.pass(serviceId);
                    Thread.sleep(2000);
                }
            } catch (NotRegisteredException | InterruptedException e) {
                e.printStackTrace();
            }

        });

        passThread.run();
    }
}
