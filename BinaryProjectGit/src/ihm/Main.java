package ihm;

import org.apache.log4j.PropertyConfigurator;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.ResourceCollection;
import org.eclipse.jetty.webapp.WebAppContext;

import javax.servlet.http.HttpServlet;

/**MAIN: Point d'entrée de l'application
 * @throws Exception lance une exception si elle est attrapée
 */
public class Main {
    public static void main(String[] args) throws Exception{

        Config config = new Config("properties/prod.properties");
        PropertyConfigurator.configure(config.getLog4jProperties());
        int portNumber = Integer.parseInt(config.getValueOfKey("port_number"));


        /**
         * Instanciation de notre servlet qui va répondre aux requêtes.
         */
        HttpServlet servlet = new Servlet();

        /**
         * Le webAppContext est l'entité qui contrôle l'environnement dans laquelle une application web
         * vie et respire. Autrement dit, cela permet de configurer le serveur.
         */
        WebAppContext webApp = new WebAppContext();

        /**
         * On ajoute notre servlet au webAppContext. Notre servlet traitera les requêtes sur tous les
         * chemins.
         */

        webApp.addServlet(new ServletHolder(servlet), "/");

        /**
         * ResourceCollection(String[] resources) instancie un collection de ressources. Une collection
         * de ressources est une collection de répertoires qui contiennent toutes les resources web
         * (html, js, ...). La première ressource est la ressource principale. Si une ressource n'est
         * pas trouvée dans la ressource principale, alors on recherche dans les autres ressources.
         */
        ResourceCollection resources = new ResourceCollection(new String[] {"webcontent", "assets"});

        /**
         * On ajoute les ressources au webAppContext.
         */
        webApp.setBaseResource(resources);

        /**
         * Création d'un serveur Jetty qui va écouter sur le port 8080.
         */
        Server server = new Server(portNumber);

        /**
         * On ajoute le webAppContext à notre serveur.
         */
        server.setHandler(webApp);

        /**
         * Lance le serveur.
         */
        server.start();
//        resultLogger.info("Serveur démarré avec succès");

        /**
         * join() is blocking until server is ready. It behaves like Thread.join() and indeed calls
         * join() of Jetty's thread pool. Everything works without this because jetty starts very
         * quickly. However if your application is heavy enough the start might take some time. Call of
         * join() guarantees that after it the server is indeed ready.
         */
        server.join();
    }
}
