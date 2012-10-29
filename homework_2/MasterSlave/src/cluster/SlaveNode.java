package cluster;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

/**
* Created with IntelliJ IDEA.
* User: Epifanov Sergey
* Date: 10/21/12
* Package: PACKAGE_NAME
*/
public class SlaveNode extends AbstractNode {

    @Override
    String performOperation(String operation, DBRecord dbRecord)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        return performOperationKernel(operation, dbRecord);
    }
}
