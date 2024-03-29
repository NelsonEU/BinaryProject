package ihm;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.servlet.DefaultServlet;


public class Servlet extends DefaultServlet {

    private Dispatcher dispatcher;

    public Servlet() {
        this.dispatcher = new Dispatcher();
    }

    /**
     * Méthode doGet.
     *
     * @param request - la requête au serveur
     * @param response - la reponse du serveur
     * @throws IOException Signale qu'une exception d'I/O quelconque s'est produite.
     * @throws ServletException si une erreur arrive avec la Servlet.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        if (request.getServletPath().equalsIgnoreCase("/")) {
            response.setContentType("text/html");
            ServletOutputStream op = response.getOutputStream();
            op.write(Files.readAllBytes(Paths.get("webcontent/header.html")));
            op.write(Files.readAllBytes(Paths.get("webcontent/home.html")));
            op.write(Files.readAllBytes(Paths.get("webcontent/footer.html")));
            //TODO ICI TRAITER LES URL CUSTOMS
            /*
             * En fonction des urls que j'aurai custom avec encoreURI();
             * LA OU JE CATCH LE RELOAD DANS LE JS (faut que ce soit apres)
             * JE FAIS UN SWITCH EN FONCTION DU LIEN JE FAIS goTournaments() ou autre
             */
        } else {
            super.doGet(request, response);
        }
    }

    /**
     * Méthode doPost.
     *
     * @param request - la requête au serveur
     * @param response - la réponse du serveur
     * @throws IOException Signale qu'une exception d'I/O quelconque s'est produite.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        this.dispatcher.operer(request, response);
    }
}
