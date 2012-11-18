package ru.csc.database.webinterface.servlet;

import ru.csc.database.core.ConsoleApp;
import ru.csc.database.core.DBRecord;
import ru.csc.database.core.HashBase;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * @author Artemii Chugreev achugr@yandex-team.ru
 *         01.11.12
 */
@Path("/")
public class InterfaceServlet {

    @GET
    @Produces(MediaType.TEXT_HTML)
    public String getMainPage() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        final HashBase base;
        try {
            FileInputStream fileInputStream = new FileInputStream("my_base1");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            base = (HashBase) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            return "sorry, we have some problems \n" + e.getMessage();
        }
        return ConsoleApp.getAll(base);

    }

}
