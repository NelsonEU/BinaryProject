package ihm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Properties;
import java.nio.charset.StandardCharsets;

public class Config {
    /**
     * Cette classe permet d'éviter le copié collé. D'abord on vérifie si le fichier existe bien, puis
     * on reg si le fichier a bien été écrit avec tout le temps une seule clé car sinon on les
     * clés/valeurs pourraient être erronées, après ttes ces vérif on récupère le fichier, puis j'ai
     * oublié ce qu'il m'a dit (il a parlé de garder les clés dans un hashSet mais je suis plus sûre)
     */
    private Properties prop = new Properties();
    private String file;
    private HashSet<String> keys = new HashSet<String>();

    /**
     * Constructeur de la classe config.
     *
     * @param file le fichier properties
     * @throws FileNotFoundException si le fichier n'est pas trouvé
     */
    public Config(String file) throws FileNotFoundException {
        this.file = file;
        File fileProp = new File(this.file);

        if (!fileProp.exists() || !fileProp.isFile()) {
            // on vérifie si le fichier existe bien
            throw new FileNotFoundException(
                    "Le fichier " + this.file + "n'existe pas ou n'est pas dans de dossier properties");
        }
        this.loadProperties();
    }

    /**
     * Méthode qui vérifie si le fichier a bien été ecrit de la bonne manière.
     *
     * @param file : fichier properties
     */
    @SuppressWarnings("resource")
    private void checkKeysProperties(String file) {
        InputStreamReader inputStream;
        try {
            inputStream = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(inputStream);
            while (br.ready()) {
                String line = br.readLine();
                if (line == null || line.contains("#") || line.trim().equals("\n")
                        || line.trim().equals("")) {
                    continue;
                }
                String[] tab = line.split("=");
                if (keys.contains(tab[0])) {
                    throw new Exception("Il existe un conflit de clés dans ce fichier properties");
                }
                keys.add(tab[0]);
            }
            br.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    /**
     * Permet de récupérer le fichier properties.
     */
    private void loadProperties() {
        FileInputStream input = null;
        try {
            input = new FileInputStream(this.file);
            checkKeysProperties(this.file);
            prop.load(input);
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        } catch (IOException exception) {
            exception.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        }
    }

    /**
     * Return la valeur de la clé dans le fichier properties.
     *
     * @param key : clé entrée en paramètre
     * @return La valeur de la clé si elle est dans le properties, null sinon
     */
    public String getValueOfKey(String key) {

        if (!this.prop.containsKey(key)) {
            return null;
        }
        return this.prop.getProperty(key);
    }

    /**
     * Renvoie la configuration du Logger.
     *
     * @return la configuration du logger dans un properties.
     */
    public Properties getLog4jProperties() {
        Properties log4jProp = new Properties();
        for (String key : this.keys) {
            if (key.startsWith("log4j.")) {
                log4jProp.put(key, this.getValueOfKey(key));
            }
        }
        return log4jProp;
    }
    /*
     * public void writeProperty(String key, String value) throws IOException {
     *
     * if (this.prop.containsKey(key)) this.prop.replace(key, value); else {
     * this.prop.setProperty(key, value); }
     *
     * //String text = "\n" + key + "=" + value; //Files.write(Paths.get(this.file), text.getBytes(),
     * StandardOpenOption.APPEND);
     *
     *
     * try (OutputStream fluxDeSortie = Files.newOutputStream(Paths.get(this.file))) {
     *
     * this.prop.store(fluxDeSortie, ""); // Ecrit la liste de properiétés sur le flux de sortie
     *
     * } catch (IOException e) { e.printStackTrace(); }
     *
     *
     * }
     *
     * public void deleteProperty(String key) {
     *
     * this.prop.remove(key); }
     */

}
