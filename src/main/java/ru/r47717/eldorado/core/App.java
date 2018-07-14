package ru.r47717.eldorado.core;

import app.Providers;
import app.Routes;
import org.eclipse.jetty.server.Server;
import ru.r47717.eldorado.core.api.ApiManager;
import ru.r47717.eldorado.core.api.ApiManagerInterface;
import ru.r47717.eldorado.core.db.DB;
import ru.r47717.eldorado.core.env.EnvManager;
import ru.r47717.eldorado.core.providers.ProviderManager;
import ru.r47717.eldorado.core.providers.ProviderManagerInterface;
import ru.r47717.eldorado.core.router.Router;
import ru.r47717.eldorado.core.router.RouterInterface;

public class App
{
    public static void main( String[] args ) throws Exception
    {
        EnvManager.parseEnv();

        RouterInterface router = new Router();
        Routes.make(router);

        ApiManagerInterface apiManager = new ApiManager();
        apiManager.build(router);

        apiManager.registerMyself();

        ProviderManagerInterface providerManager = new ProviderManager();
        Providers.build(providerManager);

        //DB.sampleQuery();

        Server server = new Server(EnvManager.getServicePort());
        server.setHandler(new BasicHandler(router));
        server.start();
        server.dumpStdErr();
        server.join();
    }
}
