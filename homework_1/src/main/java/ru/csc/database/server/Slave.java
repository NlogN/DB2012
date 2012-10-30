package ru.csc.database.server;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * User: ilya
 * Date: 25.10.12
 */
public class Slave extends Server {
    HttpServer server;
    int port;

    public Slave(int port) throws IOException {
        this.port = port;
        server = HttpServer.create(new InetSocketAddress(port), 10);
        server.createContext("/", new Server());
        server.start();
      //  System.out.println("server on port " + port + " started");
    }

    public void stop() {
        server.stop(0);
      //  System.out.println("server on port " + port + " stoped");
    }
}
