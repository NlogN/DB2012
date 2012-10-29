package base;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.StringTokenizer;


public class ConsoleApp {

    final private static String [] permittedOperations =
            {"add", "update", "delete", "get", "flush", "load", "exit"};

    public static HashBase perform(String input, HashBase base,
                            PrintWriter out) throws NoSuchAlgorithmException, IOException, ClassNotFoundException {
        StringTokenizer stringTokenizer = new StringTokenizer(input, " ,()");

        // read operation and record id (if present)
        String operation = stringTokenizer.nextToken();
        String id;
        id = stringTokenizer.hasMoreTokens() ? stringTokenizer.nextToken() : null;

        // read fields
        String[] fields = new String[10];
        int i = 0;
        while (stringTokenizer.hasMoreTokens()) {
            fields[i++] = stringTokenizer.nextToken();
        }

       // System.out.println("input = "+input);
        // make record
        DBRecord record;
        record = new DBRecord(id, fields);

        // perform operation
        switch (operation) {
            case "add":
                base.create(record);
                break;
            case "update":
                base.update(record);
                break;
            case "get":
                out.println(base.retrieve(id));
              //  System.out.println(base.retrieve(id) + "\n");
                break;
            case "delete":
                base.delete(id);
                break;
            case "flush":
                FileOutputStream fileOutputStream = new FileOutputStream("my_base");
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                objectOutputStream.writeObject(base);
                objectOutputStream.flush();
                objectOutputStream.close();
                fileOutputStream.close();
                break;
            case "load":
                FileInputStream fileInputStream = new FileInputStream("my_base");
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                base = (HashBase) objectInputStream.readObject();
                objectInputStream.close();
                fileInputStream.close();
                break;
            case "exit":
                System.out.println("end.");
                return base;
            default:
                out.print("Unknown command. Known commands are: ");
                for (String command : permittedOperations) {
                    out.print(command + ", ");
                }
                System.out.println();
                break;
        }
        return base;
    }

//    public static void main (String [] args) throws IOException, ClassNotFoundException {
//
//        // base initialize
//        HashBase base = new HashBase(2);
//        // console input (standard input)
//        Scanner in = new Scanner(System.in);
//
//        try {
//            while (in.hasNext()) {
//                String input = in.nextLine();
//                if(input.equals("esk")){
//                    System.out.println("end.");
//                    return;
//                } else{
//                    perform(input,base);
//                }
//
//            }
//
//        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//    }

//    public static void perform(String input, HashBase base) throws NoSuchAlgorithmException, IOException, ClassNotFoundException {
//        StringTokenizer stringTokenizer = new StringTokenizer(input, " ,()");
//
//        // read operation and record id (if present)
//        String operation = stringTokenizer.nextToken();
//        String id;
//        id = stringTokenizer.hasMoreTokens() ? stringTokenizer.nextToken() : null;
//
//        // read fields
//        String[] fields = new String[10];
//        int i = 0;
//        while (stringTokenizer.hasMoreTokens()) {
//            fields[i++] = stringTokenizer.nextToken();
//        }
//
//        // make record
//        DBRecord record;
//        record = new DBRecord(id, fields);
//
//        // perform operation
//        switch (operation) {
//            case "add":
//                base.create(record);
//                break;
//            case "update":
//                base.update(record);
//                break;
//            case "get":
//                System.out.println(base.retrieve(id) + "\n");
//                break;
//            case "delete":
//                base.delete(id);
//                break;
//            case "flush":
//                FileOutputStream fileOutputStream = new FileOutputStream("my_base");
//                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
//                objectOutputStream.writeObject(base);
//                objectOutputStream.flush();
//                objectOutputStream.close();
//                fileOutputStream.close();
//                break;
//            case "load":
//                FileInputStream fileInputStream = new FileInputStream("my_base");
//                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
//                base = (HashBase) objectInputStream.readObject();
//                objectInputStream.close();
//                fileInputStream.close();
//                break;
//            case "exit":
//                System.out.println("end.");
//                return;
//            default:
//                System.out.print("Unknown command. Known commands are: ");
//                for (String command : permittedOperations) {
//                    System.out.print(command + ", ");
//                }
//                System.out.println();
//                break;
//        }
//    }


}
