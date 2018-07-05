package ru.r47717.eldorado.core.env;

import java.util.Map;
import java.util.UUID;

public class EnvManager
{
    private static String serviceName;
    private static int servicePort;
    private static String consulServer;

    public static void parseEnv() {
        Map<String, String> env = System.getenv();

        serviceName = env.getOrDefault("SERVICE_NAME", UUID.randomUUID().toString());
        servicePort = Integer.parseInt(env.getOrDefault("SERVICE_PORT", "8080"));
        consulServer = env.get("CONSUL_SERVER");
    }

    public static String getServiceName() {
        return serviceName;
    }

    public static int getServicePort() {
        return servicePort;
    }

    public static String getConsulServer() {
        return consulServer;
    }
}
