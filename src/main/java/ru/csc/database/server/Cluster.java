package ru.csc.database.server;


import java.io.IOException;


/**
 * User: ilya
 * Date: 25.10.12
 */
public class Cluster {


    public static void main(String[] args) throws IOException {
        Router router = new Router(Client.routerPort);
        Master master1 = new Master(Server.mastersPorts[0]);
        Master master2 = new Master(Server.mastersPorts[1]);
        Master master3 = new Master(Server.mastersPorts[2]);
        Slave slave1 = new Slave(Integer.parseInt("8001"));
        Slave slave2 = new Slave(Integer.parseInt("8004"));
        Slave slave3 = new Slave(Integer.parseInt("8007"));
    }


}
