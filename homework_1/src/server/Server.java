package server;

import base.ConsoleApp;
import base.HashBase;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.security.NoSuchAlgorithmException;

/**
 * User: ilya
 * Date: 21.10.12
 */

public class Server implements HttpHandler {
    HashBase base = new HashBase(2);


    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 10);
        server.createContext("/", new Server());
        server.start();
        System.out.println("server.Server started");
        System.in.read();
        server.stop(0);
        System.out.println("server.Server stoped");
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

    public String replaser(String s){
        s = s.replaceAll("%3D","=");
        s = s.replaceAll("%2C",",");
        s = s.replaceAll("%2B","+");
        s = s.replaceAll("%28","(");
        s = s.replaceAll("%29",")");
        return s;
    }



}