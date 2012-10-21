import junit.framework.TestCase;

import java.io.IOException;
import java.util.Scanner;
import java.util.zip.DataFormatException;

/**
 * Created with IntelliJ IDEA.
 * User: Epifanov Sergey
 * Date: 10/21/12
 * Package: PACKAGE_NAME
 */
public class NodeTest extends TestCase {
    public void testCrud() throws IOException, DataFormatException {
        Node node = new MasterNode();
        try (Scanner in = new Scanner("commands_1.txt")) {
            while (in.hasNext()) {
                String result = node.crud(in.nextLine());
                System.out.println(result);
            }
        }
        try (Scanner in = new Scanner("commands_2.txt")) {
            while (in.hasNext()) {
                node.crud(in.nextLine());
                String result = node.crud(in.nextLine());
                System.out.println(result);
            }
        }
    }
}
