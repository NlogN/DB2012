import java.util.HashMap;

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
    public String crud(String parsedCommand) {
        HashMap<String, Object> request = parse(parsedCommand);
        String operation = (String) request.get("method");
        HashMap<String, String> record = (HashMap<String, String
                >) request.get("value");
        return performOperation(operation, record);
    }

    private HashMap<String, Object> parse(String parsedCommand) {
        return null;
    }

    abstract String performOperation(String operation, HashMap <String,
            String> record);
}
