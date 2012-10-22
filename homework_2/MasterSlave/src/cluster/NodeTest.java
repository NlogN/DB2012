package cluster;

import junit.framework.TestCase;

import java.io.FileReader;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import java.util.zip.DataFormatException;

/**
 * Created with IntelliJ IDEA.
 * User: Epifanov Sergey
 * Date: 10/21/12
 * Package: PACKAGE_NAME
 */
public class NodeTest extends TestCase {
    public void testCrud() throws IOException, DataFormatException, NoSuchAlgorithmException {
        Cluster cluster = new Cluster(3);
        Node masterNode = new MasterNode(cluster, 3);
        try (Scanner in = new Scanner(new FileReader("test_1.txt"))) {
            while (in.hasNext()) {
                String result = masterNode.crud(in.nextLine());
                System.out.println(result);
            }
        }
        try (Scanner in = new Scanner(new FileReader("test_2.txt"))) {
            while (in.hasNext()) {
                masterNode.crud(in.nextLine());
                String result = masterNode.crud(in.nextLine());
                System.out.println(result);
            }
        }
    }
}
