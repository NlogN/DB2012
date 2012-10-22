import cluster.Cluster;
import junit.framework.TestCase;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import java.util.zip.DataFormatException;

/**
 * Created with IntelliJ IDEA.
 * User: Epifanov Sergey
 * Date: 10/22/12
 * Package: cluster
 */
public class ClusterTest extends TestCase {

    PrintStream out = System.out;

    public void testName() throws
            IOException,
            DataFormatException,
            NoSuchAlgorithmException {


        Cluster cluster = new Cluster(3);
        //System.out.println(System.getProperty("user.dir"));
        Scanner in = new Scanner(new FileReader("test_1.txt"));
        out.println("\ntest_1.txt");
        while (in.hasNext()) {
            out.println(cluster.masterNode.crud(in.nextLine()));
        }
        out.println("\ntest_2.txt");
        in = new Scanner(new FileReader("test_2.txt"));
        while (in.hasNext()) {
            out.println(cluster.masterNode.crud(in.nextLine()));
        }
    }
}

