package ru.csc.database.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.csc.database.core.ConsoleApp;
import ru.csc.database.core.HashBase;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;

/**
 * User: ilya
 * Date: 21.10.12
 */

public class Server implements HttpHandler {
    HashBase base = new HashBase(2);


    @Override
    public void handle(HttpExchange exc) throws IOException {

        exc.sendResponseHeaders(200, 0);

        InputStreamReader isr = new InputStreamReader(exc.getRequestBody(), "utf-8");
        BufferedReader br = new BufferedReader(isr);
        String value = br.readLine();

        value = replaser(value);

        int k = value.indexOf("=");
        if(k!=-1){
            String command = value.substring(k+1);
            PrintWriter out = new PrintWriter(exc.getResponseBody());

            try {
                base = ConsoleApp.perform(command, base, out);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }


            out.close();
        }
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