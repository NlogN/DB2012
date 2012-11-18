package ru.csc.database.server;


import java.io.IOException;
import java.io.PrintWriter;

/**
 * User: ilya
 * Date: 25.10.12
 */
public class Cluster {


    public static void main(String[] args) throws IOException {
        PrintWriter out = new PrintWriter(System.out);
        Router router = new Router(Client.routerPort, out);
        Master master1 = new Master(Server.mastersPorts[0], out);
        Master master2 = new Master(Server.mastersPorts[1], out);
        Master master3 = new Master(Server.mastersPorts[2], out);
        Slave slave1 = new Slave(Integer.parseInt("8001"), out);
        Slave slave2 = new Slave(Integer.parseInt("8004"), out);
        Slave slave3 = new Slave(Integer.parseInt("8007"), out);

    }


}
