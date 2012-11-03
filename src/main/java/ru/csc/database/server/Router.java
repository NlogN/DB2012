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
import ru.csc.database.core.ConsoleApp;
import ru.csc.database.core.HashBase;

import java.io.*;
import java.net.InetSocketAddress;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: ilya
 * Date: 02.11.12
 */
public class Router extends Server {
    private HttpServer server;
    private int port;


    public Router(int port) throws IOException {
        super();
        this.port = port;
        server = HttpServer.create(new InetSocketAddress(port), 10);
        server.createContext("/", new MyHandler());
        server.start();
        System.out.println("server on port " + port + " started");
    }


    private void stop() {
        server.stop(0);
        System.out.println("server on port " + port + " stoped");
    }


    class MyHandler implements HttpHandler {
        public void handle(HttpExchange exc) throws IOException {
            exc.sendResponseHeaders(200, 0);

            InputStreamReader isr = new InputStreamReader(exc.getRequestBody(), "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String value = br.readLine();

            value = replaser(value);
            value = retranslateRuText(value);

            HttpPost[] posts = new HttpPost[mastersPorts.length];
            for (int i = 0; i < posts.length; i++) {
                posts[i] = new HttpPost(defaultHttp + mastersPorts[i] + "/");
            }

            PrintWriter out = new PrintWriter(exc.getResponseBody());

            int k = value.indexOf("=");
            if (k != -1) {
                String command = value.substring(k + 1);

                if (command.equals("stopR")) {
                    stop();
                } else {

                    if (command.equals("exit")) {
                        out.println("end.");
                        perform("stopsh1", posts, out);
                        perform("stopsh2", posts, out);
                        perform("stopsh3", posts, out);
                        perform("stopR", posts, out);
                        System.exit(0);
                    } else if (command.equals("stopm1")) {
                        perform(command, posts, out);
                    } else if (command.equals("stopm2")) {
                        perform(command, posts, out);
                    } else if (command.equals("stopm3")) {
                        perform(command, posts, out);
                    } else if (command.equals("stopm")) {
                        perform("stopm1", posts, out);
                        perform("stopm2", posts, out);
                        perform("stopm3", posts, out);
                    } else if (command.equals("stopsh1")) {
                        perform(command, posts, out);
                    } else if (command.equals("stopsh2")) {
                        perform(command, posts, out);
                    } else if (command.equals("stopsh3")) {
                        perform(command, posts, out);
                    } else if (command.equals("flush")) {
                        perform("flush1", posts, out);
                        perform("flush2", posts, out);
                        perform("flush3", posts, out);
                    } else if (command.equals("load")) {
                        perform("load1", posts, out);
                        perform("load2", posts, out);
                        perform("load3", posts, out);
                    } else if (command.equals("getall")) {
                        perform("getall1", posts, out);
                        perform("getall2", posts, out);
                        perform("getall3", posts, out);
                    } else {
                        perform(command, posts, out);
                    }

                }
            }

            out.close();
            exc.close();
        }


        void perform(String command, HttpPost[] posts, PrintWriter out) throws IOException {
            command = translateRuText(command);

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("command", command));
            HttpClient client = new DefaultHttpClient();

            if (command.indexOf("getall") == 0) {
                try {
                    toMaster(command, posts, out, client, nameValuePairs);
                } catch (HttpHostConnectException e) {
                    System.out.println("Required server is unavailable.");
                }
                try {
                    toSlave(command, out, client, nameValuePairs);
                } catch (HttpHostConnectException e) {
                    System.out.println("Required server is unavailable.");
                }

            } else {
                try {
                    toMaster(command, posts, out, client, nameValuePairs);
                } catch (HttpHostConnectException e) { // если мастер упал
                    try {
                        toSlave(command, out, client, nameValuePairs);
                    } catch (HttpHostConnectException e1) {
                        System.out.println("Required server is unavailable.");
                    }
                }
            }

        }

        void toMaster(String command, HttpPost[] posts, PrintWriter out, HttpClient client, List<NameValuePair> nameValuePairs) throws IOException {
            int masterPort = getMasterPortInd(command);
            HttpPost post = posts[masterPort];
            post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = client.execute(post);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String line;
            while ((line = rd.readLine()) != null) {
                out.println(line);
            }
        }

        void toSlave(String command, PrintWriter out, HttpClient client, List<NameValuePair> nameValuePairs) throws IOException {
            if (command.indexOf("get") == 0 || command.indexOf("flush") == 0 || command.indexOf("load") == 0 || command.indexOf("getall") == 0) {

                int slavePort = getSlavePort(command);
                HttpPost post1 = new HttpPost(defaultHttp + slavePort + "/");
                post1.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                try {
                    HttpResponse response = client.execute(post1);
                    BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                    String line;
                    while ((line = rd.readLine()) != null) {
                        out.println(line);
                    }
                } catch (HttpHostConnectException e1) {
                    out.println("Required server is unavailable.");
                }


            }
        }


    }


}

