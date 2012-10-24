package base;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Sergey Epifanov
 * Date: 9/13/12
 */

public class DBRecord implements Serializable {
    final String id;
    private final String[] fields;
    DBRecord next;

    DBRecord(String id, String[] fields) {
        this.id = id;
        this.fields = fields;
    }

    public String toString() {
        String result = id;
        if (fields != null) {
            for (String field : fields) {
                // TO DO: make it more strict
                if (field != null) {
                    result += " " + field;
                }
            }
        }
        return result;
    }

}
