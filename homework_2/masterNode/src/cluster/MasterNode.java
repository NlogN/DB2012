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
    private final Cluster myClaster;

    MasterNode(Cluster cluster) {
        this.slavesCount = cluster.getSlavesCount();
        this.myClaster = cluster;
    }

    @Override
    String performOperation(String operation, DBRecord dbRecord)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String id = dbRecord.id;
        SlaveNode slaveNode = getNode(id);
        String slaveResult = slaveNode.performOperation(operation, dbRecord);
        String masterResult = pop(operation, dbRecord);
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
        Cluster cluster;
        int slaveNumber = HashBase.hash(recordId, slavesCount);
        return myClaster.getSlave(slaveNumber);
    }
}
