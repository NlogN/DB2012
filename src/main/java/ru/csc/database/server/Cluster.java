package ru.csc.database.server;


import java.io.IOException;

/**
 * User: ilya
 * Date: 25.10.12
 */
public class Cluster {
//    Master[] masters;
//    Slave[] slaves;
//    int port;
//
//    public Cluster(int port) throws IOException {
//        this.port = port;
//        int masterPort1 = port;
//        int masterPort2 = port + 3;
//        int masterPort3 = port + 6;
//        Master master1 = new Master(masterPort1);
//        Master master2 = new Master(masterPort2);
//        Master master3 = new Master(masterPort3);
//        masters = new Master[]{master1, master2, master3};
//
//        int slavePort11 = masterPort1 + 1;
//        int slavePort21 = masterPort2 + 1;
//        int slavePort31 = masterPort3 + 1;
//
//
//        Slave slave11 = new Slave(slavePort11);
//        Slave slave21 = new Slave(slavePort21);
//        Slave slave31 = new Slave(slavePort31);
//
//
//        slaves = new Slave[]{slave11, slave21, slave31};
//    }
//
//    public void stop() {
//        for (Master master : masters) {
//            master.stop();
//        }
//        for (Slave slave : slaves) {
//            slave.stop();
//        }
//    }
//
//    public void sh1Stop() {
//        masters[0].stop();
//        slaves[0].stop();
//        System.out.println("shard1 stoped.");
//    }
//
//    public void sh2Stop() {
//        masters[1].stop();
//        slaves[1].stop();
//        System.out.println("shard2 stoped.");
//    }
//
//    public void sh3Stop() {
//        masters[2].stop();
//        slaves[2].stop();
//        System.out.println("shard3 stoped.");
//    }
//
//    public void m1Stop() {
//        masters[0].stop();
//        System.out.println("Master1 stoped.");
//    }
//
//    public void m2Stop() {
//        masters[1].stop();
//        System.out.println("Master2 stoped.");
//    }
//
//    public void m3Stop() {
//        masters[2].stop();
//        System.out.println("Master3 stoped.");
//    }
//
//    public void mStop() {
//        for (Master master : masters) {
//            master.stop();
//        }
//        System.out.println("Masters stoped.");
//    }




   public static void main(String[] args) throws IOException {
       Router router = new Router(Integer.parseInt("8010"));
       Master master1 = new Master(Integer.parseInt("8000"));
       Master master2 = new Master(Integer.parseInt("8003"));
       Master master3 = new Master(Integer.parseInt("8006"));
       Slave slave1 = new Slave(Integer.parseInt("8001"));
       Slave slave2 = new Slave(Integer.parseInt("8004"));
       Slave slave3 = new Slave(Integer.parseInt("8007"));

    }

}
