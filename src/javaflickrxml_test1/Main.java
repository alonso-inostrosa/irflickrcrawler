/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javaflickrxml_test1;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.String;
import java.net.MalformedURLException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alonso
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {

        ConfigInfo info = null; //Contiene "apikey" y "secret".

        try {
            info = new ConfigInfo( /*path*/);
        } catch (IOException ex) {
            System.out.println("Error al leer archivo propiedades");
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("URL:" + info.getUrl());

        int maxThreads = info.getMaxThreads();

        Vector<String> groupsID = new Vector<String>();

        //Obtener palabras para generar la coleccion
        //Damos nombre de archivo de palabras para buscar grupos y archivo donde guardar los resultados de grupo
        StringProcessor fileManager = new StringProcessor(/*info.getWordsFilename(), info.getGroupsResult()*/);
        String[] words = fileManager.fileToStrings();

        //Crear un Vector con palabras a buscar para cada Thread
        Vector wordsVocabulary = new Vector(maxThreads);
        for( int i = 0; i < maxThreads; i++){
            wordsVocabulary.add(i, new Vector());
        }

        //Cada Vector almacena un subconjunto de la coleccion de palabras
        int cont = 0;
        while ( cont < words.length ) {
            ((Vector) wordsVocabulary.elementAt( cont%maxThreads )).add(words[cont]);
            cont++;
        }

        Thread[] thread = new Thread[maxThreads];

        for( int i = 0; i < maxThreads; i++){
            thread[i] = new Thread(new ThreadExploreGroups(i, (Vector) wordsVocabulary.elementAt( i ),
                                        groupsID, info));
            thread[i].start();
        }

        //Esperamos a que los threads terminen
        for( int i = 0; i < maxThreads; i++){
            thread[i].join();
        }

        ///**********************///
        //Abrimos archivo para guardar resultados de grupo
        fileManager.openFileToWrite();


        //Guardar groupsID recuperados a un archivo
        System.out.println("Nro. de groupId's:" + groupsID.size());
        /*Iterator iter = groupsID.iterator();
        while (iter.hasNext()) {
            String gid = (String) iter.next();
            System.out.println("GID: " + gid);
        }*/
        String[] groupsRes = new String[groupsID.size()];
        groupsRes = (String[]) groupsID.toArray( groupsRes );
        fileManager.addStringToFile( groupsRes );


            
        //Cerramos archivo donde guardamos resultados de grupo
        fileManager.closeFileToWrite();
        ///**********************///
        
    }
}
