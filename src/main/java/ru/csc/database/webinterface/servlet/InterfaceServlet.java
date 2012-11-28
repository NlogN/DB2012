package ru.csc.database.webinterface.servlet;

import de.neuland.jade4j.Jade4J;
import ru.csc.database.core.ConsoleApp;
import ru.csc.database.core.DBRecord;
import ru.csc.database.core.HashBase;
import ru.csc.database.server.Client;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;

/**
 * @author Artemii Chugreev achugr@yandex-team.ru
 *         01.11.12
 */
@Path("/")
public class InterfaceServlet {

    @GET
    @Produces(MediaType.TEXT_HTML)
    public String getMainPage(){
        try {
            return Jade4J.render("src/main/resources/mainPage.jade", new HashMap<String, Object>());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GET
    @Path("search")
    @Produces(MediaType.TEXT_HTML)
    public String getSearchResult(@QueryParam("query") final String wordForSearch) throws NoSuchAlgorithmException, IOException, ClassNotFoundException {
        StringWriter sw = new StringWriter();
        PrintWriter writer = new PrintWriter(sw);
        Client.perform(wordForSearch, writer);
        Reader reader = new StringReader(sw.toString());
        BufferedReader bufferedReader = new BufferedReader(reader);

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            sb.append(line).append("<br>");
        }
        bufferedReader.close();
        return sb.toString();
    }

    // a(href="/all") all records
//    @GET
//    @Path("search")
//    @Produces(MediaType.TEXT_HTML)
//    public String getSearchResult(@QueryParam("query") final String wordForSearch) throws NoSuchAlgorithmException, IOException, ClassNotFoundException {
//        HashBase base;
//        final String query = "get(" + wordForSearch + ")";
//        try {
//            FileInputStream fileInputStream = new FileInputStream("my_base1");
//            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
//            base = (HashBase) objectInputStream.readObject();
//            objectInputStream.close();
//            fileInputStream.close();
//        } catch (Exception e) {
//            return "sorry, we have some problems \n" + e.getMessage();
//        }
//        StringWriter sw = new StringWriter();
//        PrintWriter printWriter = new PrintWriter(sw);
//
//        base = ConsoleApp.perform(query, base, printWriter);
//
//        printWriter.flush();
//        return sw.getBuffer().toString();
//    }

//    @GET
//    @Path("all")
//    @Produces(MediaType.TEXT_HTML)
//    public String getAllRecords() throws NoSuchAlgorithmException, UnsupportedEncodingException {
//        final HashBase base;
//        try {
//            FileInputStream fileInputStream = new FileInputStream("my_base1");
//            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
//            base = (HashBase) objectInputStream.readObject();
//            objectInputStream.close();
//            fileInputStream.close();
//        } catch (Exception e) {
//            return "sorry, we have some problems \n" + e.getMessage();
//        }
//        List<DBRecord> allRecords = ConsoleApp.getAll(base);
//        StringBuilder sb = new StringBuilder();
//        for(DBRecord dbRecord : allRecords){
//            sb.append(dbRecord.toString()).append("<br>");
//        }
//        return sb.toString();
//    }

}
