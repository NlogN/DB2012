package ru.csc.database.server;


import ru.csc.database.core.HashBase;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;


/**
 * User: ilya
 * Date: 21.10.12
 */

public class Server {
    protected HashBase base;
    public static String defaultHttp = "http://127.0.0.1:";
    static int port1 = 8000;
    static int[] mastersPorts = {port1, port1 + 3, port1 + 6};

    Server() {
        base = new HashBase();
    }


    public static void main(String[] args) throws IOException {
        if(args.length==2){
            if(args[0].equals("master")){
                Master master = new Master(Integer.parseInt(args[1]));
            }
            if(args[0].equals("slave")){
                Slave slave = new Slave(Integer.parseInt(args[1]));
            }
            if(args[0].equals("router")){
                Router router = new Router(Integer.parseInt(args[1]));
            }
        }else{
            System.out.println("incorrect parameter");
        }

    }


    public static String replaser(String s){
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

    public static int getSlavePort(String command) {
        return getMasterPort(command) + 1;
    }

    public static int getMasterPort(String command) {
        return mastersPorts[getMasterPortInd(command)];
    }

    public static int getMasterPortInd(String command) {
        if (command.equals("flush1")) {
            return 0;
        } else if (command.equals("flush2")) {
            return 1;
        } else if (command.equals("flush3")) {
            return 2;
        } else if (command.equals("load1")) {
            return 0;
        } else if (command.equals("load2")) {
            return 1;
        } else if (command.equals("load3")) {
            return 2;
        }
        if (command.equals("stopm1")) {
            return 0;
        }
        if (command.equals("stopm2")) {
            return 1;
        }
        if (command.equals("stopm3")) {
            return 2;
        }
        if (command.equals("stopsh1")) {
            return 0;
        }
        if (command.equals("stopsh2")) {
            return 1;
        }
        if (command.equals("stopsh3")) {
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
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            return res;
        }
        return 0;
    }
}