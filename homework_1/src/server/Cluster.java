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
        this.port=port;
        int masterPort = port;
        int slave1Port = masterPort+1;
        int slave2Port = masterPort+2;
        int slave3Port = masterPort+3;

        master = new Master(masterPort);
        Slave slave1 = new Slave(slave1Port);
        Slave slave2 = new Slave(slave2Port);
        Slave slave3 = new Slave(slave3Port);
        slaves = new Slave[]{slave1,slave2,slave3};
    }

//    public String perform(String command){
//           return "";
//    }

    public void stop(){
        master.stop();
        for (Slave slave:slaves){
             slave.stop();
        }
    }
}
