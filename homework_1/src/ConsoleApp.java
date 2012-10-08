import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * Created with IntelliJ IDEA.
 * User: Sergey Epifanov
 * Date: 9/14/12
 */

public class ConsoleApp {

    private static String [] permittedOperations =
            {"add", "update", "delete", "get", "flush", "load", "exit"};

    public static void main (String [] args) throws IOException, ClassNotFoundException {

        // base initialize
        HashBase base = new HashBase(2);
        // console input (standard input)
        Scanner in = new Scanner(System.in);

        try {
            while (in.hasNext()) {
                String input = in.nextLine();
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

                // make record
                DBRecord record;
                record = new DBRecord(id, fields);

                // perform operation
                if (operation.equals("add")) {
                    base.create(record);
                } else if (operation.equals("update")) {
                    base.update(record);
                } else if (operation.equals("get")) {
                    System.out.println(base.retrieve(id) + "\n");
                } else if (operation.equals("delete")) {
                    base.delete(id);
                } else if (operation.equals("flush")) {
                    FileOutputStream fileOutputStream = new FileOutputStream("my_base");
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                    objectOutputStream.writeObject(base);
                    objectOutputStream.flush();
                    objectOutputStream.close();
                    fileOutputStream.close();
                } else if (operation.equals("load")) {
                    FileInputStream fileInputStream = new FileInputStream("my_base");
                    ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                    base = (HashBase) objectInputStream.readObject();
                    objectInputStream.close();
                    fileInputStream.close();
                } else if (operation.equals("exit")) {
                    System.out.println("end.");
                    return;
                } else {
                    System.out.print("Unknown command. Known commands are: ");
                    for (String command : permittedOperations) {
                        System.out.print(command + ", ");
                    }
                    System.out.println();
                }
            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }


}
