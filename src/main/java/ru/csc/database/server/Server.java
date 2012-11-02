package ru.csc.database.server;


import ru.csc.database.core.HashBase;

import java.io.IOException;


/**
 * User: ilya
 * Date: 21.10.12
 */

public class Server {
    protected HashBase base;

    Server() {
        base = new HashBase();
    }


    public String replaser(String s){
        s = s.replaceAll("%3D","=");
        s = s.replaceAll("%2C",",");
        s = s.replaceAll("%2B","+");
        s = s.replaceAll("%28","(");
        s = s.replaceAll("%29",")");
        s = s.replaceAll("%24","\\$");
        return s;
    }

    protected static String retranslateRuText(String s) {
        for (int i = 1040; i <= 1103; i++) {
            s = s.replaceAll("\\$" + i + "\\$", Character.toString((char) i));
        }
        return s;
    }

    protected static String translateRuText(String s) {
        for (int i = 1040; i <= 1103; i++) {
            s = s.replaceAll(Character.toString((char) i), "\\$" + i + "\\$");
        }
        return s;
    }

    public static void main(String[] args) throws IOException {
        if(args.length==2){
            if(args[0].equals("master")){
                Master master = new Master(Integer.parseInt(args[1]));
            }
            if(args[0].equals("slave")){
                Slave slave = new Slave(Integer.parseInt(args[1]));
            }
        }else{
            System.out.println("incorrect parameter");
        }

    }
}