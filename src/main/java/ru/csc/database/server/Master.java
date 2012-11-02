package ru.csc.database.server;

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
import ru.csc.database.core.ConsoleApp;

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
    private HttpServer server;
    private int port;


    public Master(int port) throws IOException {
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


            int k = value.indexOf("=");
            if (k != -1) {
                String command = value.substring(k + 1);
                if (command.indexOf("stopm") == 0) {
                    stop();
                } else {
                    PrintWriter out = new PrintWriter(exc.getResponseBody());
                    if (command.indexOf("stopsh") == 0) {
                        updateSlave(command,out);
                        stop();
                    } else {

                        try {
                            base = ConsoleApp.perform(command, base, out);
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }

                        if (command.indexOf("get(") != 0 && command.indexOf("flush") != 0) {
                            updateSlave(command,out);
                        }
                    }
                    out.close();
                }
            }
            exc.close();
        }


        public void updateSlave(String command, PrintWriter out) throws IOException {
            HttpClient client = new DefaultHttpClient();
            command = translateRuText(command);

            int slavePort = getSlavePort(command);

            HttpPost post = new HttpPost(defaultHttp + slavePort + "/");
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("command", command));
            post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpResponse response = client.execute(post);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String line;
            while ((line = rd.readLine()) != null) {
                out.println(line);
            }
        }


    }

//    public static void main(String[] args) throws IOException {
//      Master master = new Master(Integer.parseInt("8006"));
//
//    }
}