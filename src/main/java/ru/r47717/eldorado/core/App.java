package ru.r47717.eldorado.core;

import app.PublishedApi;
import org.eclipse.jetty.server.Server;
import ru.r47717.eldorado.core.api.ApiManager;
import ru.r47717.eldorado.core.api.ApiManagerInterface;
import ru.r47717.eldorado.core.consul.ConsulManager;
import ru.r47717.eldorado.core.env.EnvManager;

public class App
{
    public static void main( String[] args ) throws Exception
    {
        EnvManager.parseEnv();

        ApiManagerInterface apiManager = new ApiManager();
        PublishedApi.make(apiManager);
        ConsulManager.registerMyself(apiManager);

        Server server = new Server(EnvManager.getServicePort());
        server.setHandler(new BasicHandler());
        server.start();
        server.dumpStdErr();
        server.join();
    }
}
