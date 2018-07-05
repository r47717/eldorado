package ru.r47717.eldorado.core;

import org.eclipse.jetty.server.Server;
import ru.r47717.eldorado.core.env.EnvManager;

public class App
{
    public static void main( String[] args ) throws Exception
    {
        EnvManager.parseEnv();

        Server server = new Server(EnvManager.getServicePort());
        server.setHandler(new BasicHandler());
        server.start();
        server.dumpStdErr();
        server.join();
    }
}
