package ru.csc.database.server;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * User: ilya
 * Date: 22.10.12
 */


public class Client {
    private static HttpClient client = new DefaultHttpClient();
    static int routerPort = 8010;


    public static void main(String[] args) throws IOException {
        PrintWriter pw = new PrintWriter(System.out, true);
        Scanner in = new Scanner(System.in);
        while (in.hasNext()) {
            String command = in.nextLine();
            perform(command, pw);
        }
    }

    public static void perform(String command, PrintWriter out) throws IOException {
        command = command.replaceAll(" ", "");
        if (isCorrect(command)) {

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("command", command));

            HttpPost post1 = new HttpPost(Server.defaultHttp + routerPort + "/");
            post1.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

            try {
                HttpResponse response = client.execute(post1);
                BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                String line;
                while ((line = rd.readLine()) != null) {
                    out.println(line);
                }
            } catch (HttpHostConnectException e1) {
                out.println("Router is unavailable.");
            }
            if (command.equals("exit")) {
                out.println("client is stopped.");
                System.exit(0);
            }

        } else {
            out.println("incorrect command.");
        }
    }



    final static Pattern p1 = Pattern.compile("^((get)|(delete)|(add))[(][A-Za-zА-Яа-я]+[)]$");
    final static Pattern p2 = Pattern.compile("^((add)|(update))[(][A-Za-zА-Яа-я]+,[+]{0,1}[0-9]+[)]$");
    final static Pattern p3 = Pattern.compile("^((flush)|(load)|)[0-9]{0,1}$");
    final static Pattern p4 = Pattern.compile("^((getall)|(stopR)|(exit))$");
    final static Pattern p5 = Pattern.compile("^(stopm)[1-3]{0,1}$");
    final static Pattern p6 = Pattern.compile("^(stopsh)[1-3]{1}$");

    public static boolean isCorrect(final String command) {
        return p1.matcher(command).matches() || p2.matcher(command).matches() || p3.matcher(command).matches() || 
                p4.matcher(command).matches() || p5.matcher(command).matches() || p6.matcher(command).matches();
    }
}