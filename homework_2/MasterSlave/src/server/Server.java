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
            out.println(command);
        }
        Cluster cluster = new Cluster(3);
        try {
            for (String command:commands){
                System.out.println(cluster.masterNode.crud(command));
            }

        } catch (DataFormatException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        cluster.masterNode.print();

        out.close();
        exc.close();
    }

    public String replaser(String s){
        s = s.replaceAll("%3D","=");
        s = s.replaceAll("%2C",",");
        s = s.replaceAll("%2B","+");
       // s = s.replaceAll(",+",", ");
        return s;
    }

}