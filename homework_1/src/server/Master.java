package server;

import base.ConsoleApp;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.security.NoSuchAlgorithmException;

/**
 * User: ilya
 * Date: 25.10.12
 */
public class Master extends Server{
    HttpServer server;
    int port;

    public Master(int port) throws IOException {
        this.port = port;
        server = HttpServer.create(new InetSocketAddress(port), 10);
        server.createContext("/", new Server());
        server.start();
        System.out.println("server on port " + port + " started");
    }

    public void stop() {
        server.stop(0);
        System.out.println("server on port " + port + " stoped");
    }

    @Override
    public void handle(HttpExchange exc) throws IOException {

        InputStreamReader isr =  new InputStreamReader(exc.getRequestBody(),"utf-8");
        BufferedReader br = new BufferedReader(isr);
        String value = br.readLine();
        value = replaser(value);

        String[] commands = value.split("&");

        for (int i = 0; i < commands.length; i++) {
            String command = commands[i];
            if (command.charAt(command.length() - 1) == '=') {
                commands[i] = command.substring(0, command.length() - 1);
            }
        }

        exc.sendResponseHeaders(200, 0);
        PrintWriter out = new PrintWriter(exc.getResponseBody());

        for (String command:commands){

            try {
                ConsoleApp.perform(command, base, out);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (ClassNotFoundException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }


        out.close();
        exc.close();
    }
}
