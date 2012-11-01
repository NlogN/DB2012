package ru.csc.database.webinterface;

import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.webapp.WebAppContext;

/**
 * @author Artemii Chugreev achugr@yandex-team.ru
 *         31.10.12
 */
public class ServerInitializer {
    private int port = 8080;

    public void startServer() {
        Server server = new Server();
// TODO configure webAppContexts by spring
        Connector connector = new SelectChannelConnector();
        connector.setPort(port);
        server.addConnector(connector);

        WebAppContext root = new WebAppContext("src/main/webapp/", "/");
        server.setHandlers(new Handler[]{root});

        try {
            server.start();
        } catch (Exception e) {
            throw new RuntimeException("something went wrong " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        ServerInitializer serverInitializer = new ServerInitializer();
        serverInitializer.startServer();
    }
}
