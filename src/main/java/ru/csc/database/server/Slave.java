package ru.csc.database.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import ru.csc.database.core.ConsoleApp;
import ru.csc.database.core.DBRecord;

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
public class Slave extends Server {
    private HttpServer server;
    private int port;


    public Slave(int port) throws IOException {
        super();
        this.port = port;
        server = HttpServer.create(new InetSocketAddress(port), 10);
        server.createContext("/", new MyHandler());
        server.start();
        System.out.println("server on port " + port + " started");
    }


    private void stop() {
        System.out.println("server on port " + port + " stoped");
        server.stop(0);
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
            if(k!=-1){
                String command = value.substring(k+1);

                if (command.indexOf("stopsh") == 0) {
                    stop();
                } else {
                    PrintWriter out = new PrintWriter(exc.getResponseBody());
//                    if(command.equals("getall")){
//                        try {
//                            for (DBRecord dbRecord : base.retrieveAll()) {
//                                out.println("#"+dbRecord.toString());
//                            }
//                        } catch (NoSuchAlgorithmException e) {
//                            e.printStackTrace();
//                        }
//                    } else{
                        try {
                            base = ConsoleApp.perform(command, base, out);
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
//                    }

                    out.close();
                }
            }
        }

    }


}
