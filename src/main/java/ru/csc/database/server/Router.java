package ru.csc.database.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * User: ilya
 * Date: 02.11.12
 */
public class Router extends Server {
    private HttpServer server;
    private int port;
    //private final PrintWriter out;


    public Router(int port) throws IOException {
        super();
       // this.out = out;
        this.port = port;
        server = HttpServer.create(new InetSocketAddress(port), 10);
        server.createContext("/", new MyHandler());
        server.start();
        System.out.println("router on port " + port + " started");
       // out.flush();
    }


    private void stop() {
        server.stop(0);
        System.out.println("router on port " + port + " stoped");
    //    out.flush();
        System.exit(0);
    }


    class MyHandler extends BaseHttpHandler {

        protected void perform(final String value, PrintWriter out) throws IOException {

           // out = new PrintWriter(exc.getResponseBody());

            int k = value.indexOf("=");
            if (k != -1) {
                String command = value.substring(k + 1);

                if (command.equals("stopR")) {
                    stop();
                } else {

                    if (command.equals("exit")) {
                        performCommand("stopsh1", out);
                        performCommand("stopsh2", out);
                        performCommand("stopsh3", out);
                        stop();
                    } else if (command.equals("stopm1")) {
                        performCommand(command, out);
                    } else if (command.equals("stopm2")) {
                        performCommand(command, out);
                    } else if (command.equals("stopm3")) {
                        performCommand(command, out);
                    } else if (command.equals("stopm")) {
                        performCommand("stopm1", out);
                        performCommand("stopm2", out);
                        performCommand("stopm3", out);
                    } else if (command.equals("stopsh1")) {
                        performCommand(command, out);
                    } else if (command.equals("stopsh2")) {
                        performCommand(command, out);
                    } else if (command.equals("stopsh3")) {
                        performCommand(command, out);
                    } else if (command.equals("flush")) {
                        performCommand("flush1", out);
                        performCommand("flush2", out);
                        performCommand("flush3", out);
                    } else if (command.equals("load")) {
                        performCommand("load1", out);
                        performCommand("load2", out);
                        performCommand("load3", out);
                    } else if (command.equals("getall")) {
                        performCommand("getall1", out);
                        performCommand("getall2", out);
                        performCommand("getall3", out);
                    } else {
                        performCommand(command, out);
                    }

                }
            }

            out.close();
        }


        void performCommand(String command, PrintWriter out) throws IOException {
            command = translateRuText(command);

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("command", command));
            HttpClient client = new DefaultHttpClient();

            if (command.indexOf("getall") == 0||command.indexOf("stopsh") == 0) {
                try {
                    toMaster(command, out, client, nameValuePairs);
                } catch (HttpHostConnectException e) {
                    out.println("Required master is unavailable.");

                }
                try {
                    toSlave(command, out, client, nameValuePairs);
                } catch (HttpHostConnectException e) {
                    out.println("Required slave is unavailable.");
                }

            } else {
                try {
                    toMaster(command, out, client, nameValuePairs);
                } catch (HttpHostConnectException e) { // если мастер упал
                    out.println("Required master is unavailable.");
                    try {
                        toSlave(command, out, client, nameValuePairs);
                    } catch (HttpHostConnectException e1) {
                        out.println("Required slave is unavailable.");
                    }
                }
            }
        out.flush();
        }

        void toMaster(String command, PrintWriter out, HttpClient client, List<NameValuePair> nameValuePairs) throws IOException {

            int masterPort = getMasterPort(command);

            HttpPost post = new HttpPost(defaultHttp + masterPort + "/");

            post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = client.execute(post);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String line;
            while ((line = rd.readLine()) != null) {
                out.println(line);
            }
            out.flush();
        }

        void toSlave(String command, PrintWriter out, HttpClient client, List<NameValuePair> nameValuePairs) throws IOException {
            if (command.indexOf("get") == 0 || command.indexOf("flush") == 0 || command.indexOf("load") == 0 || command.indexOf("getall") == 0 || command.indexOf("stopsh") == 0) {

                int slavePort = getSlavePort(command);
                HttpPost post1 = new HttpPost(defaultHttp + slavePort + "/");
                post1.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                HttpResponse response = client.execute(post1);
                BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                String line;
                while ((line = rd.readLine()) != null) {
                    out.println(line);
                }
            }
            out.flush();
        }


    }


}

