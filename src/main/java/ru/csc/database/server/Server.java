package ru.csc.database.server;


import ru.csc.database.core.HashBase;

import java.io.IOException;

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
        if (args.length == 2) {
            if (args[0].equals("master")) {
                Master master = new Master(Integer.parseInt(args[1]));
            }
            if (args[0].equals("slave")) {
                Slave slave = new Slave(Integer.parseInt(args[1]));
            }
            if (args[0].equals("router")) {
                Router router = new Router(Integer.parseInt(args[1]));
            }
        } else {
            System.out.println("incorrect parameter");
        }

    }


    public static int getSlavePort(String command) {
        return getMasterPort(command) + 1;
    }

    public static int getMasterPort(String command) {
        int num = getMasterPortId(command);
        return mastersPorts[num];
    }

    public static int getMasterPortId(String command) {
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
        if (command.equals("getall1")) {
            return 0;
        }
        if (command.equals("getall2")) {
            return 1;
        }
        if (command.equals("getall3")) {
            return 2;
        }

        return getIndex(command, mastersPorts.length);
    }

    public static int getIndex(String command, int maxHashValue) {
        int ind1 = command.indexOf("(");
        int ind2 = command.indexOf(",");
        if (ind2 == -1) {
            ind2 = command.indexOf(")");
        }
        if (ind1 < ind2) {
            String key = command.substring(ind1 + 1, ind2);
            return hash(key, maxHashValue);
        }
        return 0;
    }

    public static int hash(String s, int maxHashValue) {
        int h = 19;
        for (int i = 0; i < s.length(); i++) {
            h = 31 * h + s.charAt(i);
        }
        int res = h % maxHashValue;
        if (res > 0) {
            return res;
        }
        return 0;
    }

}