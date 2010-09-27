package javaflickrxml_test1;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author alonso
 */
public class ConfigInfo {

    private String apiKey; // = "d335579449e935290c47d99d9c087fa8";
    private String sharedSecret; // = "6bf78ab2a5d0c3a2";
    private String nsid; // = "25802393@N02";
    
    private String baseFilename; //Nombre base para los archivos xml generados.
    private String wordsFilename; //Nombre de archivo con las palabras para coleccion
    private String groupsResult;

    private int page;
    private int per_page;
    private int maxPages;

    private int maxThreads;

    private String url;


    public ConfigInfo( ) throws IOException
    {
        this("./setup.properties");
    }

    /**
     * Constructor, recupera los datos de acceso a Flickr desde un archivo
     * de properties.
     * @param path es la ruta al archivo de propiedades.
     * @throws IOException
     */
    public ConfigInfo(String path) throws IOException
    {
        InputStream in = null;
        Properties properties = null;

        in = getClass().getResourceAsStream( path );
        properties = new Properties();

        properties.load(in);
        in.close();

        apiKey = properties.getProperty("apiKey");
        sharedSecret = properties.getProperty("secret");
        baseFilename = properties.getProperty("base_filename");
        wordsFilename = properties.getProperty("words_filename");
        groupsResult = properties.getProperty("group_result");

        page = Integer.valueOf( properties.getProperty("page") );
        per_page = Integer.valueOf( properties.getProperty("per_page") );
        maxPages = Integer.valueOf(properties.getProperty("maxPages"));

        maxThreads = Integer.valueOf(properties.getProperty("maxThreads"));

        url = properties.getProperty("url");

        //System.out.println("ApiKey:" + apiKey + " - Secret:" + sharedSecret);
    }





    /////////////Getters & Setters//////////////
    public String getApiKey() {
        return apiKey;
    }

    public String getNsid() {
        return nsid;
    }

    public String getSharedSecret() {
        return sharedSecret;
    }

    public String getBaseFilename() {
        return baseFilename;
    }

    public String getUrl() {
        return url;
    }

    public String getWordsFilename() {
        return wordsFilename;
    }

    public int getPage() {
        return page;
    }

    public int getPer_page() {
        return per_page;
    }

    public int getMaxPages() {
        return maxPages;
    }

    public String getGroupsResult() {
        return groupsResult;
    }

    public int getMaxThreads() {
        return maxThreads;
    }



}
