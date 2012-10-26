package server;

import base.ConsoleApp;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.*;
import java.net.InetSocketAddress;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: ilya
 * Date: 25.10.12
 */
public class Master extends Server {
    HttpClient client = new DefaultHttpClient();
    HttpServer server;
    int port;


    public Master(int port) throws IOException {
        this.port = port;
        server = HttpServer.create(new InetSocketAddress(port), 10);
        server.createContext("/", new MyHandler());
        server.start();
        //  System.out.println("server on port " + port + " started");
    }

    public void stop() {
        server.stop(0);
        //  System.out.println("server on port " + port + " stoped");
    }


    class MyHandler implements HttpHandler {
        public void handle(HttpExchange exc) throws IOException {

            InputStreamReader isr = new InputStreamReader(exc.getRequestBody(), "utf-8");
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


            for (String command : commands) {
                try {
                    ConsoleApp.perform(command, base, out);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                if (!command.substring(0, 3).equals("get")) {
                    updateSlave(command);
                }


            }


            out.close();
            exc.close();
        }


        public void updateSlave(String command) throws IOException {

            HttpClient client1 = new DefaultHttpClient();

            int slavePort = Client.getSlavePort(command);

            HttpPost post = new HttpPost(Client.defaultHttp + slavePort + "/");
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair(command, ""));
            post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            //System.out.println(command + "to slave");

            client1.execute(post);
            //  HttpResponse response = client.execute(post);
//                BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
//                String line = "";
//                while ((line = rd.readLine()) != null) {
//                    System.out.println(line);
//                }


        }
    }
}