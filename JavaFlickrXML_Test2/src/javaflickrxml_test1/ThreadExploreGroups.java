/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javaflickrxml_test1;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Hashtable;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.xmlrpc.XmlRpcClient;
import org.apache.xmlrpc.XmlRpcException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Esta clase se utiliza para buscar y escribir los resultados de la busqueda
 * de grupos
 * @author alonso
 */
public class ThreadExploreGroups implements Runnable {

    private XmlRpcClient xmlrpc = null;
    private Vector words;
    private int id;
    private ConfigInfo info;

    Vector<String> groupsID;    //Almacenar resultados

    public ThreadExploreGroups(int id, Vector words, Vector<String> groupsID, ConfigInfo info){
        this.words = words;
        this.id = id;
        this.groupsID = groupsID;
        this.info = info;
    }

    public void run() {

        int page = 0;//info.getPage();
        int per_page = info.getPer_page();
        int totalCount = 0;
        int totalPages = 0;
        int maxPages = info.getMaxPages();
        int currentElement = 0;

        Vector params = new Vector(); //Vector de parametros a enviar en llamada
        Hashtable struct = new Hashtable(); //Cada uno de los parametros a enviar
        String result = null; //Almacena el resultado retornado por la llamada
        
        try {
            System.out.println("Hilo " + id);

            /****************/
            Document document = null;
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            XmlRpcClient xmlrpc = null;
            
            try {

                xmlrpc = new XmlRpcClient(info.getUrl());

                //Itera sobre palabras para buscar grupos
                int i = 0;
                while ( i < words.size() ) {

                    struct.put("api_key", info.getApiKey());            //Clave API
                    page = info.getPage();
                    params.add(struct);
                    System.out.println("Buscando grupos con: " + words.elementAt(i));

                    do{
                        struct.put("text", words.elementAt(i));                       //Texto a buscar
                        struct.put("per_page", String.valueOf(per_page));   //Elementos por pagina
                        struct.put("page", String.valueOf(page));           //Pagina

                        //Ejecuta consulta y obtiene resultados
                        result = (String) xmlrpc.execute("flickr.groups.search", params);

                        document = builder.parse(new ByteArrayInputStream(result.getBytes()));
                        NodeList nodes = document.getElementsByTagName("groups");
                        totalCount = Integer.parseInt(nodes.item(0).getAttributes().getNamedItem("total").getNodeValue());
                        totalPages = Integer.parseInt(nodes.item(0).getAttributes().getNamedItem("pages").getNodeValue());

                        //Recorremos todos los elementos "group" de la pagina
                        nodes = document.getElementsByTagName("group");
                        for (currentElement = 0; currentElement < nodes.getLength(); currentElement++) {
                            Node node = nodes.item(currentElement);
                            String groupId = (String) node.getAttributes().getNamedItem("nsid").getNodeValue();
                            groupsID.add("ThreadId_" + id + "&" + groupId + "&" + words.elementAt(i));          //Guardamos el ID del grupo
                        }

                        //System.out.println("Page:" + page + "Total Pages" + totalPages);
                        page++;
                    }while( page < totalPages && page < maxPages);

                    i++;
                }

            } catch (MalformedURLException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (XmlRpcException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            /*****************/

        } catch (Exception exp) {
            exp.printStackTrace();
        } finally {
            // Don't do anything here
        }
    }

}
