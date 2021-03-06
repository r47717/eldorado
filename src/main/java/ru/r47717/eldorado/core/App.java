package ru.r47717.eldorado.core;

import app.Providers;
import app.Routes;
import org.eclipse.jetty.server.Server;
import ru.r47717.eldorado.core.api.SimpleApiManager;
import ru.r47717.eldorado.core.api.ApiManager;
import ru.r47717.eldorado.core.env.EnvManager;
import ru.r47717.eldorado.core.handler.BasicHandler;
import ru.r47717.eldorado.core.providers.SimpleProviderManager;
import ru.r47717.eldorado.core.providers.ProviderManager;
import ru.r47717.eldorado.core.router.SimpleRouter;
import ru.r47717.eldorado.core.router.Router;

public class App
{
    public static void main( String[] args ) throws Exception
    {
        EnvManager.parseEnv();

        Router router = new SimpleRouter();
        Routes.make(router);

        ApiManager apiManager = new SimpleApiManager();
        apiManager.build(router);

        apiManager.registerMyself();

        ProviderManager providerManager = new SimpleProviderManager();
        Providers.build(providerManager);

        //DB.sampleQuery();

        Server server = new Server(EnvManager.getServicePort());
        server.setHandler(new BasicHandler(router));
        server.start();
        server.dumpStdErr();
        server.join();
    }
}
