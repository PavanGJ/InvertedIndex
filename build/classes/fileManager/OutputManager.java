/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filemanager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pavanjoshi
 */
public class OutputManager {
    private static File outputFileHandler;
    private static BufferedWriter outputWriter;
    
    public static Boolean prepareOutputFile(String outputFilePath){
        try {
            outputFileHandler = new File(outputFilePath);
            if(!outputFileHandler.exists())
                outputFileHandler.createNewFile();
            outputWriter = new BufferedWriter(new FileWriter(outputFileHandler.getAbsoluteFile()));            
        } catch (IOException ex) {
            Logger.getLogger(OutputManager.class.getName()).log(Level.SEVERE, null, ex);
            try {
                outputWriter.close();
            } catch (IOException ex1) {
                Logger.getLogger(OutputManager.class.getName()).log(Level.SEVERE, null, ex1);
            }
            return false;
        }
        catch (Exception e){
            Logger.getLogger(OutputManager.class.getName()).log(Level.SEVERE, null, e);
            try {
                outputWriter.close();
            } catch (IOException ex) {
                Logger.getLogger(OutputManager.class.getName()).log(Level.SEVERE, null, ex);
            }
            return false;
        }
        return true;
    }
    public static void writeOutput(String outputString) {
        try {
            outputWriter.write(outputString);
        } catch (IOException ex) {
            Logger.getLogger(OutputManager.class.getName()).log(Level.SEVERE, null, ex);
            try {
                outputWriter.close();
            } catch (IOException ex1) {
                Logger.getLogger(OutputManager.class.getName()).log(Level.SEVERE, null, ex1);
            }
            System.out.println("Error : Could not write to file");
        }
    }
    public static void closeFile(){
        try {
            outputWriter.close();
        } catch (IOException ex) {
            Logger.getLogger(OutputManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
