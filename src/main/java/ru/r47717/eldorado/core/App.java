package ru.r47717.eldorado.core;

import org.eclipse.jetty.server.Server;

public class App
{
    public static void main( String[] args ) throws Exception {
        Server server = new Server(8080);
        server.setHandler(new BasicHandler());
        server.start();
        server.dumpStdErr();
        server.join();
    }
}
