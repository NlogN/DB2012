package cluster;

/**
 * Created with IntelliJ IDEA.
 * User: Epifanov Sergey
 * Date: 10/22/12
 * Package: PACKAGE_NAME
 */
public class Cluster {
    public final MasterNode masterNode;
    private final int slavesCount;
    private final SlaveNode [] slaveNodes;

    public Cluster(int slavesCount) {
        this.masterNode = new MasterNode(this);
        this.slavesCount = slavesCount;
        slaveNodes = new SlaveNode[slavesCount];
        for (int i = 0; i < slavesCount; i++) {
            slaveNodes[i] = new SlaveNode();
        }
    }

    SlaveNode getSlave(int number) {
        return slaveNodes[number];
    }

    int getSlavesCount() {
        return slavesCount;
    }
}
