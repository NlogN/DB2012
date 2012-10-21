/**
 * Created with IntelliJ IDEA.
 * User: Epifanov Sergey
 * Date: 10/21/12
 * Package: PACKAGE_NAME
 */

public interface Node {

    /**
     * perform crud - operations
     * @param parsedCommand command in inner format
     *          {"method":"create", "value":{"name":"marina", "tel":"+72921"}}
     * @return for "create", "update", "delete" methods: "success" or "fail".
     * For "retrieve" method: record in inner format
     */

    String crud(String parsedCommand);
}
