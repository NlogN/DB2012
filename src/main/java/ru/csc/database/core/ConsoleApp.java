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
                switch (operation) {
                    case "add":
                        base.create(record);
                        break;
                    case "update":
                        base.update(record);
                        break;
                    case "get":
                        out.println(base.retrieve(id));
                        break;
                    case "getall":
                        for (DBRecord dbRecord : base.retrieveAll()) {
                            System.out.println("mu");
                            out.println(dbRecord);
                        }
                        break;
                    case "delete":
                        base.delete(id);
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
            }
        }


        return base;
    }



}
