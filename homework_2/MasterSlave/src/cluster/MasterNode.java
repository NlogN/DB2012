package cluster;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

/**
* Created with IntelliJ IDEA.
* User: Epifanov Sergey
* Date: 10/21/12
* Package: PACKAGE_NAME
*/

public class MasterNode extends AbstractNode {

    private final int slavesCount;
    private final Cluster myCluster;

    MasterNode(Cluster cluster, int slavesCount) {
        this.slavesCount = slavesCount;
        this.myCluster = cluster;
    }

    @Override
    String performOperation(String operation, DBRecord dbRecord)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String id = dbRecord.id;
        SlaveNode slaveNode = getNode(id);
        String  slaveResult = slaveNode.performOperation(operation, dbRecord);
        String masterResult = performOperationKernel(operation, dbRecord);
        if (operation.equals("retrieve")) {
            return slaveResult;
        }
        if (masterResult.equals("success") && slaveResult.equals("success")) {
            return "success";
        }
        return "fail";
    }

    private SlaveNode getNode(String recordId)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        int slaveNumber = HashBase.hash(recordId, slavesCount);
        return myCluster.getSlave(slaveNumber);
    }
}