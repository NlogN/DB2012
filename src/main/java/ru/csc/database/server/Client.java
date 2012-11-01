package ru.csc.database.server;

import ru.csc.database.core.HashBase;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.*;
import java.security.NoSuchAlgorithmException;
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
    public static String defaultHttp = "http://127.0.0.1:";
    private static HttpClient client = new DefaultHttpClient();
    static int port = 8000;
    static int[] mastersPorts = {port, port + 3, port + 6};

    public static void main(String[] args) throws IOException {

        Cluster cluster = new Cluster(port);

        HttpPost[] posts = new HttpPost[mastersPorts.length];
        for (int i = 0; i < posts.length; i++) {
            posts[i] = new HttpPost(defaultHttp + mastersPorts[i] + "/");
        }

        try {
            Scanner in = new Scanner(System.in);
            while (in.hasNext()) {
                String command = in.nextLine();
                switch (command) {
                    case "esk":
                        System.out.println("end.");
                        break;
                    case "ms1":
                        cluster.m1Stop();
                        break;
                    case "ms2":
                        cluster.m2Stop();
                        break;
                    case "ms3":
                        cluster.m3Stop();
                        break;
                    case "ms":
                        cluster.mStop();
                        break;
                    case "sh1":
                        cluster.sh1Stop();
                        break;
                    case "sh2":
                        cluster.sh2Stop();
                        break;
                    case "sh3":
                        cluster.sh3Stop();
                        break;
                    case "flush":
                        perform("flush1",posts);
                        perform("flush2",posts);
                        perform("flush3",posts);
                        break;
                    case "load":
                        perform("load1", posts);
                        perform("load2", posts);
                        perform("load3", posts);
                        break;
                    default:
                      perform(command, posts);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        cluster.stop();

    }

    static void perform(String command, HttpPost[] posts) throws IOException {
        command = command.replaceAll(" ", "");
        if (isCorrect(command)) {
            balancer(command, posts[getMasterPortInd(command)]);
        } else{
            System.out.println("Unknown command.");
        }
    }

    static void balancer(String command, HttpPost post) throws IOException {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair("command", command));

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

    public static int getSlavePort(String command) {
        return getMasterPort(command) + 1;
    }

    public static int getMasterPort(String command) {
        return mastersPorts[getMasterPortInd(command)];
    }

    public static int getMasterPortInd(String command) {
        switch (command) {
            case "flush1":
                return 0;
            case "flush2":
                return 1;
            case "flush3":
                return 2;
            case "load1":
                return 0;
            case "load2":
                return 1;
            case "load3":
                return 2;
        }
        return hash(command, 3);
    }

    public static int hash(String command, int maxHashValue) {
        int ind1 = command.indexOf("(");
        int ind2 = command.indexOf(",");
        if (ind2 == -1) {
            ind2 = command.indexOf(")");
        }
        if (ind1 < ind2) {
            String key = command.substring(ind1 + 1, ind2);

            int res = 0;
            try {
                res = HashBase.hash(key, maxHashValue);
            } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return res;
        }
        return 0;
    }


    public static boolean isCorrect(String command) {
        Pattern p1 = Pattern.compile("^((get)|(delete)|(add))[(][A-Za-z0-9]+[)]$");
        Pattern p2 = Pattern.compile("^((add)|(update))[(][A-Za-z0-9]+,[+]{0,1}[0-9]+[)]$");
        Pattern p3 = Pattern.compile("^((flush)|(load)|)[0-9]{1}$");
        Pattern p4 = Pattern.compile("^getall$");
        Matcher m1 = p1.matcher(command);
        Matcher m2 = p2.matcher(command);
        Matcher m3 = p3.matcher(command);
        Matcher m4 = p4.matcher(command);
        return m1.matches()||m2.matches()||m3.matches()||m4.matches();
    }


}