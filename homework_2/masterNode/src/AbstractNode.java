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
 * implements common part of classes MasterNode and SlaveNode
 */

public abstract class AbstractNode implements Node {

    @Override
    public String crud(String parsedCommand) throws DataFormatException {
        HashMap<String, Object> request = parse(parsedCommand);
        String operation = (String) request.get("method");
        HashMap<String, String> record = (HashMap<String, String
                >) request.get("value");
        return performOperation(operation, record);
    }

    private HashMap<String, Object> parse(String parsedCommand) throws
            DataFormatException {

        StringTokenizer tokens = new StringTokenizer(parsedCommand, " ,()");

        // read operation
        String operation = tokens.nextToken();
        HashMap<String, String> record = new HashMap();
        // read other fields
        while (tokens.hasMoreTokens()) {
            String [] key_value =
                    tokens.nextToken().split("=", 2);
            if (key_value.length == 2) {
                record.put(key_value[0], key_value[1]);
            } else {
                throw new DataFormatException("формат данных должен быть " +
                        "вида add(id=ivanov, name=Иванов, " +
                        "tel=+7921AAABBCC)");
            }
        }

        HashMap<String, Object> result = new HashMap();
        result.put("method", operation);
        result.put("value", record);
        return result;
    }

    abstract String performOperation(String operation, HashMap <String,
            String> record);
}
