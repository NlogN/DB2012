package ru.csc.database.webinterface.servlet;

import de.neuland.jade4j.Jade4J;
import ru.csc.database.server.Client;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

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

    private StringWriter sw = new StringWriter();
    private PrintWriter writer = new PrintWriter(sw);

    @GET
    @Path("search")
    @Produces(MediaType.TEXT_HTML)
    public String getSearchResult(@QueryParam("query") final String wordForSearch) throws NoSuchAlgorithmException, IOException, ClassNotFoundException {
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

}
