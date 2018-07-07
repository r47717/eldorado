package ru.r47717.eldorado.core;

import app.Routes;
import org.eclipse.jetty.server.Server;
import ru.r47717.eldorado.core.api.ApiManager;
import ru.r47717.eldorado.core.api.ApiManagerInterface;
import ru.r47717.eldorado.core.consul.ConsulManager;
import ru.r47717.eldorado.core.env.EnvManager;
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

        Server server = new Server(EnvManager.getServicePort());
        server.setHandler(new BasicHandler(router));
        server.start();
        server.dumpStdErr();
        server.join();
    }
}
