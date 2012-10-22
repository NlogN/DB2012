package cluster;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Sergey Epifanov
 * Date: 9/13/12
 */

public class DBRecord implements Serializable {
    final String id;
    private final HashMap<String, String> fields;
    DBRecord next;

    DBRecord(String id, HashMap<String, String> fields) {
        this.id = id;
        this.fields = fields;
    }

    public String toString() {
        String result = "id=" + fields.get("id");
        for (String field : fields.keySet()) {
            if (!field.equals("id") && !field.equals("method")) {
                result += ", " + field + "=" + fields.get(field);
            }
        }
        return result;
    }

}
