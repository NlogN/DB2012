package cluster;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.zip.DataFormatException;

/**
* Created with IntelliJ IDEA.
* User: Epifanov Sergey
* Date: 10/21/12
* Package: PACKAGE_NAME
*/

/**
 * implements common part of classes cluster.MasterNode and cluster.SlaveNode
 */

public abstract class AbstractNode implements Node {


    HashBase hashBase;
    AbstractNode() {
        hashBase = new HashBase();
    }

    @Override
    public String crud(String parsedCommand)
            throws DataFormatException,
            NoSuchAlgorithmException, UnsupportedEncodingException {
        HashMap<String, Object> request = commandToHashMap(parsedCommand);
        String operation = (String) request.get("method");
        HashMap<String, String> record =
                (HashMap<String, String>) request.get("value");
        String id = record.get("id");
        DBRecord dbRecord = new DBRecord(id, record);
        return performOperation(operation, dbRecord);
    }

    abstract String performOperation(String operation, DBRecord dbRecord)
            throws NoSuchAlgorithmException, UnsupportedEncodingException;

    String pop(String operation, DBRecord dbRecord)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        DBRecord result = null;
        switch(operation) {
            case "create":
                hashBase.create(dbRecord);
                break;
            case "retrieve":
                if (this.getClass().getName().equals("cluster.SlaveNode")) {
                    result = hashBase.retrieve(dbRecord.id);
                }
                break;
            case "update":
                hashBase.update(dbRecord);
                break;
            case "delete":
                hashBase.delete(dbRecord.id);
                break;
            default:
                System.out.print("Unknown command. Known commands are: ");
                final String [] permittedOperations =
                        {"create", "retrieve", "update", "delete"};
                for (String command : permittedOperations) {
                    System.out.print(command + ", ");
                }
                System.out.println();
                break;
        }
        if (result != null) {
            return result.toString();
        }
        return "success"; // TO DO: if there was no exceptions
    }

    private HashMap<String, Object> commandToHashMap(String parsedCommand)
            throws DataFormatException {

        StringTokenizer tokens = new StringTokenizer(parsedCommand, " ,");
        HashMap<String, String> record = new HashMap();
        while (tokens.hasMoreTokens()) {
            String [] key_value = tokens.nextToken().split("=");
            if (key_value.length == 2) {
                record.put(key_value[0], key_value[1]);
            } else {
                throw new DataFormatException("Data format must be like:\n" +
                        "method=create, id=ivanov, name=Иванов, " +
                        "tel=+7921AAABBCC\n" + "or\n" +
                        "method=retrieve, id=ivanov\n" +
                        "but there is token: " + key_value[0]);
            }
        }

        // make record
        HashMap<String, Object> result = new HashMap();
        result.put("method", record.get("method"));
        result.put("value", record);
        result.put("id", record.get("id")); // TO DO: record.get("id") exists?
        return result;
    }
}
