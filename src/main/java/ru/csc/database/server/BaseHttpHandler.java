package ru.csc.database.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
* Created by: pulser at 04.11.12 19:17
*/
abstract class BaseHttpHandler implements HttpHandler {

    public void handle(final HttpExchange exc) throws IOException {
        exc.sendResponseHeaders(200, 0);

        final InputStreamReader isr = new InputStreamReader(exc.getRequestBody(), "utf-8");
        final BufferedReader br = new BufferedReader(isr);
        String value = br.readLine();

        value = Server.replaser(value);
        value = Server.retranslateRuText(value);

        PrintWriter out =  new PrintWriter(exc.getResponseBody());
        perform(value, out);
        exc.close();
    }

    abstract protected void perform(final String value, PrintWriter out) throws IOException;

}
