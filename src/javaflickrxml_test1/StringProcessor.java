package javaflickrxml_test1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author teresabn
 */

public class StringProcessor {

    private String fileRead;
    private String fileWrite;
    BufferedWriter output = null;

    public StringProcessor(){
        this.fileRead = "./words_collection.txt";
        this.fileWrite = "./group_result_collection.txt";
        System.out.println("Archivo a leer: " + fileRead + " - Archivo a escribir: " + fileWrite);
    }

    public StringProcessor( String fileRead, String Write ){
        this.fileRead = fileRead;
        this.fileWrite = fileWrite;
    }

    public String[] fileToStrings(String fileRead) {
         this.fileRead = fileRead;
         return fileToStrings();
    }

    public String[] fileToStrings() {

        String line = null;
        BufferedReader input = null;
        String[] lines = null;
        ArrayList tmp = new ArrayList();
        try {
            input = new BufferedReader(new FileReader(fileRead));
            while ((line = input.readLine()) != null) {
                tmp.add(line);
            }
        } catch (Exception ex) {
            System.out.println("Archivo no encontrado " + fileRead);
            //ex.printStackTrace();
        }
        lines = (String[]) tmp.toArray(new String[1]);
        return lines;

    }

    public void openFileToWrite(){
        try {
            output = new BufferedWriter(new FileWriter(fileWrite, true));
        } catch (IOException ex) {
            System.out.println("Error al abrir archivo para escritura - " + fileWrite);
            Logger.getLogger(StringProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void closeFileToWrite(){
        try {
            output.close();
        } catch (IOException ex) {
            System.out.println("Error al cerrar archivo para escritura - " + fileWrite);
            Logger.getLogger(StringProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //StringProcessor.addStringToFile("5", "MyFile.txt");
    public void addStringToFile(String[] s) {

        try {
            //output = new BufferedWriter(new FileWriter(fileWrite, true));
            for( int i = 0; i < s.length; i++){
                output.append(s[i]);
                output.newLine();
            }
            //output.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
