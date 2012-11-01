package ru.csc.database.core;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.StringTokenizer;


public class ConsoleApp {

    final private static String [] permittedOperations =
            {"add", "update", "delete", "get", "getall", "flush", "load",
                    "exit"};

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


        DBRecord record;
        record = new DBRecord(id, fields);

        if(operation.indexOf("flush")==0){
            FileOutputStream fileOutputStream = new FileOutputStream("my_base"+operation.substring(5));
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(base);
            objectOutputStream.flush();
            objectOutputStream.close();
            fileOutputStream.close();
        }else{
            if(operation.indexOf("load")==0){
                FileInputStream fileInputStream = new FileInputStream("my_base"+operation.substring(4));
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                base = (HashBase) objectInputStream.readObject();
                objectInputStream.close();
                fileInputStream.close();
            }else{
                if (operation.equals("add")) {
                    base.create(record);

                } else if (operation.equals("update")) {
                    base.update(record);

                } else if (operation.equals("get")) {
                    out.println(base.retrieve(id));

                } else if (operation.equals("getall")) {
                    for (DBRecord dbRecord : base.retrieveAll()) {
                        out.println(dbRecord);
                    }

                } else if (operation.equals("delete")) {
                    base.delete(id);

                } else if (operation.equals("exit")) {
                    System.out.println("end.");
                    return base;
                } else {
                    out.print("Unknown command. Known commands are: ");
                    for (String command : permittedOperations) {
                        out.print(command + ", ");
                    }
                    System.out.println();

                }
            }
        }


        return base;
    }



}
