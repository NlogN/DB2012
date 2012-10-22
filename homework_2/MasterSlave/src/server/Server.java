package server;

/**
 * User: ilya
 * Date: 21.10.12
 */
import java.io.*;
import java.net.InetSocketAddress;
import java.security.NoSuchAlgorithmException;
import java.util.zip.DataFormatException;

import cluster.Cluster;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;


public class Server implements HttpHandler {



    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 10);
        server.createContext("/", new Server());
        server.start();
        System.out.println("Server started");
        System.in.read();
        server.stop(0);
        System.out.println("Server stoped");
    }

    @Override
    public void handle(HttpExchange exc) throws IOException {

        InputStreamReader isr =  new InputStreamReader(exc.getRequestBody());
        BufferedReader br = new BufferedReader(isr);
        String value = br.readLine();
//        String[] commands = value.split("=");

        exc.sendResponseHeaders(200, 0);
        PrintWriter out = new PrintWriter(exc.getResponseBody());
        out.println(value);
//        for (String command:commands){
//            out.println(command);
//        }
//        Cluster cluster = new Cluster(3);
//        try {
//
//            cluster.masterNode.crud(value.substring(0,value.length()-1));
//        } catch (DataFormatException e) {
//            e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }

        out.close();
        exc.close();
    }

}