package server;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
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
    public static void main(String[] args) throws FileNotFoundException {

        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost("http://127.0.0.1:8000/");
        try {

            Scanner in = new Scanner(System.in);
                while (in.hasNext()) {
                    String input = in.nextLine();
                    if(input.equals("esk")){
                        System.out.println("end.");
                        return;
                    } else{
                        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                        nameValuePairs.add(new BasicNameValuePair(input, ""));
                        post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                        HttpResponse response = client.execute(post);
                        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                        String line = "";
                        while ((line = rd.readLine()) != null) {
                            System.out.println(line);
                        }
                    }

                }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
