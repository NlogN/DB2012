package server;

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
                        cluster.m1stop();
                        break;
                    case "ms2":
                        cluster.m2stop();
                        break;
                    case "ms3":
                        cluster.m3stop();
                        break;
                    default:

                        command = command.replaceAll(" ", "");
                        if (isCorrect(command)) {
                            balancer(command, posts[getMasterPortInd(command)]);
                        }
                }


            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        cluster.stop();

    }

    static void balancer(String command, HttpPost post) throws IOException {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair(command, ""));

        try {
            post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = client.execute(post);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String line = "";
            while ((line = rd.readLine()) != null) {
                System.out.println(line);
            }
        } catch (HttpHostConnectException e) {
            if (command.substring(0, 3).equals("get")) {

                int slavePort = getSlavePort(command);
                HttpPost post1 = new HttpPost(defaultHttp + slavePort + "/");
                post1.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                HttpResponse response = client.execute(post1);
                BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                String line = "";
                while ((line = rd.readLine()) != null) {
                    System.out.println(line);
                }

            } else {
                System.out.println("Cервер недоступен.");
            }
        }

    }

    public static int getSlavePort(String command) {
        int hash = hash(command);
        int slavePort = getMasterPort(command)+ 1 + (hash % 2);
        return slavePort;
    }

    public static int getMasterPort(String command) {
        int hash = hash(command);
        return mastersPorts[hash % 3];
    }

    public static int getMasterPortInd(String command) {
        int hash = hash(command);
        return hash % 3;
    }

    public static int hash(String command) {
        int ind1 = command.indexOf("(");
        int ind2 = command.indexOf(",");
        if (ind2 == -1) {
            ind2 = command.indexOf(")");
        }
        if (ind1 < ind2) {
            String key = command.substring(ind1 + 1, ind2);

            return key.length();
        }
        return 0;
    }

    //TODO реализовать
    public static boolean isCorrect(String command) {
        return true;
    }
}
