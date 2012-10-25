package server;


import java.io.IOException;

/**
 * User: ilya
 * Date: 25.10.12
 */
public class Cluster {
    Master master;
    Slave[] slaves;
    int port;

    public Cluster(int port) throws IOException {
        this.port = port;
        int masterPort = port;
        int slavePort1 = masterPort + 1;
        int slavePort2 = masterPort + 2;

        master = new Master(masterPort);
        Slave slave1 = new Slave(slavePort1);
        Slave slave2 = new Slave(slavePort2);
        slaves = new Slave[]{slave1, slave2};
    }

    public void stop() {
        master.stop();
        for (Slave slave : slaves) {
            slave.stop();
        }
    }
}
