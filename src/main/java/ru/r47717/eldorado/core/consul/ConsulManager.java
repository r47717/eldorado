package ru.r47717.eldorado.core.consul;

import com.orbitz.consul.*;
import com.orbitz.consul.model.health.Service;
import com.orbitz.consul.model.health.ServiceHealth;
import ru.r47717.eldorado.core.env.EnvManager;

import java.util.*;


public class ConsulManager
{
    private static Thread passThread;

    public boolean consulHealthCheck() {
        String url = EnvManager.getConsulServer();
        if (url == null) {
            return false;
        }

        Consul consul = Consul.builder().withUrl(url).build();
        return consul != null;
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

    public static void registerService(List<String> tags, Map<String, String> meta) {
        String url = EnvManager.getConsulServer();

        if (url == null || url.isEmpty()) {
            return;
        }

        Consul consul = Consul.builder().withUrl(url).build();
        AgentClient agentClient = consul.agentClient();

        String serviceName = EnvManager.getServiceName();
        final String serviceId = serviceName;

        agentClient.register(EnvManager.getServicePort(), 3L, serviceName, serviceId, tags, meta);

        passThread = new Thread(() -> {
            try {
                while(true) {
                    //System.out.println("pass");
                    agentClient.pass(serviceId);
                    Thread.sleep(2000);
                }
            } catch (NotRegisteredException | InterruptedException e) {
                e.printStackTrace();
            }

        });

        passThread.start();
    }

    public static String getAliveProviderUrl(String name) {
        String consulUrl = EnvManager.getConsulServer();
        Consul consul = Consul.builder().withUrl(consulUrl).build();
        HealthClient healthClient = consul.healthClient();

        List<ServiceHealth> nodes = healthClient.getHealthyServiceInstances(name).getResponse();
        if (nodes.size() > 0) {
            Service service = nodes.get(0).getService();
            Map<String, String> meta = service.getMeta();
            String url = meta.get("url");

            return url;
        }

        System.out.println("No alive service instances with name " + name);

        return null;
    }
}
