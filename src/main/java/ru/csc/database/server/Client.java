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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: ilya
 * Date: 22.10.12
 */


public class Client {
    private static HttpClient client = new DefaultHttpClient();
    static int routerPort = 8010;
    private static final PrintWriter out = new PrintWriter(System.out);


    public static void main(String[] args) throws IOException {

            Scanner in = new Scanner(System.in);
            while (in.hasNext()) {
                String command = in.nextLine();
                command = command.replaceAll(" ", "");
                if (isCorrect(command)) {
                    command = translateRuText(command);

                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                    nameValuePairs.add(new BasicNameValuePair("command", command));

                    HttpPost post1 = new HttpPost(Server.defaultHttp + routerPort  + "/");
                    post1.setEntity(new UrlEncodedFormEntity(nameValuePairs));

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

                } else{
                    System.out.println("incorrect command.");
                }
            }
    }


    public static boolean isCorrect(String command) {
        Pattern p1 = Pattern.compile("^((get)|(delete)|(add))[(][A-Za-zА-Яа-я]+[)]$");
        Pattern p2 = Pattern.compile("^((add)|(update))[(][A-Za-zА-Яа-я]+,[+]{0,1}[0-9]+[)]$");
        Pattern p3 = Pattern.compile("^((flush)|(load)|)[0-9]{0,1}$");
        Pattern p4 = Pattern.compile("^((getall)|(stopR)|(exit))$");
        Pattern p5 = Pattern.compile("^(stopm)[1-3]{0,1}$");
        Pattern p6 = Pattern.compile("^(stopsh)[1-3]{1}$");
        Matcher m1 = p1.matcher(command);
        Matcher m2 = p2.matcher(command);
        Matcher m3 = p3.matcher(command);
        Matcher m4 = p4.matcher(command);
        Matcher m5 = p5.matcher(command);
        Matcher m6 = p6.matcher(command);
        return m1.matches()||m2.matches()||m3.matches()||m4.matches()||m5.matches()||m6.matches();
    }

    private static String translateRuText(String s) {
        for (int i = 1040; i <= 1103; i++) {
            s = s.replaceAll(Character.toString((char) i), "\\$" + i + "\\$");
        }
        return s;
    }


}