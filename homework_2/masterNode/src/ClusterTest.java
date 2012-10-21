import cluster.Cluster;
import junit.framework.TestCase;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.zip.DataFormatException;

/**
 * Created with IntelliJ IDEA.
 * User: Epifanov Sergey
 * Date: 10/22/12
 * Package: cluster
 */
public class ClusterTest extends TestCase {
    public void testName() throws
            IOException,
            DataFormatException,
            NoSuchAlgorithmException {

        Cluster cluster = new Cluster(3);
        cluster.masterNode.crud("{\"method\":\"create\", " +
                "\"value\":{\"id\":\"marina\", \"tel\":\"+72921\"}}.");
        System.out.println(
                cluster.masterNode.crud("{\"method\":\"retrieve\", " +
                        "\"value\":{\"id\":\"marina\"}}."));
    }
}

