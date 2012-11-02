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

            if (value.equals("stopR")) {
                stop();
            } else {
                HttpPost[] posts = new HttpPost[mastersPorts.length];
                for (int i = 0; i < posts.length; i++) {
                    posts[i] = new HttpPost(defaultHttp + mastersPorts[i] + "/");
                }

                int k = value.indexOf("=");
                if (k != -1) {
                    String command = value.substring(k + 1);

                    if (command.equals("exit")) {
                        System.out.println("end.");
                        // cluster.stop();
                        perform("sh1", posts);
                        perform("sh2", posts);
                        perform("sh3", posts);
                        System.exit(0);

                    } else if (command.equals("ms1")) {
                        perform(command, posts);

                    } else if (command.equals("ms2")) {
                        perform(command, posts);

                    } else if (command.equals("ms3")) {
                        perform(command, posts);

                    } else if (command.equals("ms")) {
                        perform("ms1", posts);
                        perform("ms2", posts);
                        perform("ms3", posts);

                    } else if (command.equals("sh1")) {
                        // cluster.sh1Stop();
                        perform(command, posts);
                    } else if (command.equals("sh2")) {
                        // cluster.sh2Stop();
                        perform(command, posts);
                    } else if (command.equals("sh3")) {
                        // cluster.sh3Stop();
                        perform(command, posts);
                    } else if (command.equals("flush")) {
                        perform("flush1", posts);
                        perform("flush2", posts);
                        perform("flush3", posts);

                    } else if (command.equals("load")) {
                        perform("load1", posts);
                        perform("load2", posts);
                        perform("load3", posts);

                    } else {
                        perform(command, posts);
                    }
                }
            }
        }


        void perform(String command, HttpPost[] posts) throws IOException {

            command = translateRuText(command);
            if (command.indexOf("getall") == 0) {
                for (HttpPost post : posts) {
                    balancer(command, post);
                }
            } else {
                balancer(command, posts[getMasterPortInd(command)]);
            }

        }

        void balancer(String command, HttpPost post) throws IOException {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("command", command));
            HttpClient client = new DefaultHttpClient();

            try {
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(post);
                BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                String line;
                while ((line = rd.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (HttpHostConnectException e) { // если мастер упал

                if (command.indexOf("get") == 0 || command.indexOf("flush") == 0 || command.indexOf("load") == 0) {

                    int slavePort = getSlavePort(command);
                    HttpPost post1 = new HttpPost(defaultHttp + slavePort + "/");
                    post1.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    try {
                        HttpResponse response = client.execute(post1);
                        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                        String line;
                        while ((line = rd.readLine()) != null) {
                            System.out.println(line);
                        }
                    } catch (HttpHostConnectException e1) {
                        System.out.println("Required server is unavailable.");
                    }


                } else {
                    System.out.println("Required server is unavailable.");
                }

            }

        }


    }


}

