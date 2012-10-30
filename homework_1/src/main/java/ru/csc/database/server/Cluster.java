package ru.csc.database.server;


import java.io.IOException;

/**
 * User: ilya
 * Date: 25.10.12
 */
public class Cluster {
    Master[] masters;
    Slave[] slaves;
    int port;

    public Cluster(int port) throws IOException {
        this.port = port;
        int masterPort1 = port;
        int masterPort2 = port + 3;
        int masterPort3 = port + 6;
        Master master1 = new Master(masterPort1);
        Master master2 = new Master(masterPort2);
        Master master3 = new Master(masterPort3);
        masters = new Master[]{master1, master2, master3};

        int slavePort11 = masterPort1 + 1;
        int slavePort12 = masterPort1 + 2;
        int slavePort21 = masterPort2 + 1;
        int slavePort22 = masterPort2 + 2;
        int slavePort31 = masterPort3 + 1;
        int slavePort32 = masterPort3 + 2;

        Slave slave11 = new Slave(slavePort11);
        Slave slave12 = new Slave(slavePort12);
        Slave slave21 = new Slave(slavePort21);
        Slave slave22 = new Slave(slavePort22);
        Slave slave31 = new Slave(slavePort31);
        Slave slave32 = new Slave(slavePort32);

        slaves = new Slave[]{slave11, slave12, slave21, slave22, slave31, slave32};
    }

    public void stop() {
        for (Master master : masters) {
            master.stop();
        }
        for (Slave slave : slaves) {
            slave.stop();
        }
    }

    public void m1Stop() {
        masters[0].stop();
        System.out.println("Master1 stoped");
    }

    public void m2Stop() {
        masters[1].stop();
        System.out.println("Master2 stoped");
    }

    public void m3Stop() {
        masters[2].stop();
        System.out.println("Master3 stoped");
    }

    public void mStop() {
        for (Master master : masters) {
            master.stop();
        }
    }

}
