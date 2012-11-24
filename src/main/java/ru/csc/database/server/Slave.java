package ru.csc.database.server;


import com.sun.net.httpserver.HttpServer;
import ru.csc.database.core.ConsoleApp;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.security.NoSuchAlgorithmException;

/**
 * User: ilya
 * Date: 25.10.12
 */
public class Slave extends Server {
    private HttpServer server;
    private int port;

    public Slave(int port) throws IOException {
        super();
        this.port = port;
        server = HttpServer.create(new InetSocketAddress(port), 10);
        server.createContext("/", new MyHandler());
        server.start();
        System.out.println("slave on port " + port + " started");
    }


    private void stop() {
        server.stop(0);
        System.out.println("slave on port " + port + " stoped");
    }


    class MyHandler extends BaseHttpHandler {

        protected void perform(final String value, PrintWriter out) throws IOException {
            int k = value.indexOf("=");
            if (k != -1) {
                String command = value.substring(k + 1);

                if (command.startsWith("stopsh")) {
                    stop();
                } else {
                    if (command.startsWith("getall")) {
                        try {
                            ConsoleApp.print(base, out, "Slave port " + port);
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            base = ConsoleApp.perform(command, base, out);
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }

                    out.close();
                }
            }
        }

    }


}
