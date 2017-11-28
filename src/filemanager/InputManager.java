/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filemanager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pavanjoshi
 */
public class InputManager {
    private File inputFileHandler;
    private BufferedReader inputReader;
    private Map<Integer,String[]> queryTerms;
    
    public Boolean readFile(String inputFilePath){
        try {
            inputFileHandler = new File(inputFilePath);
            inputReader = new BufferedReader(new FileReader(inputFileHandler.getAbsoluteFile()));
            String inputLine;
            int index = 0;
            queryTerms = new HashMap<>();
            while(inputReader.ready()&&(inputLine = inputReader.readLine())!=null){
                queryTerms.put(index++, inputLine.split(" "));
            }
        }
        catch(FileNotFoundException e){
            Logger.getLogger(InputManager.class.getName()).log(Level.SEVERE, null, e);
            try {
                inputReader.close();
            } catch (IOException ex) {
                Logger.getLogger(InputManager.class.getName()).log(Level.SEVERE, null, ex);
            }
            return false;
        } catch (IOException ex) {
            Logger.getLogger(InputManager.class.getName()).log(Level.SEVERE, null, ex);
            try {
                inputReader.close();
            } catch (IOException e) {
                Logger.getLogger(InputManager.class.getName()).log(Level.SEVERE, null, e);
            }
            return false;
        }
        return true;
    }
    public String[] queryTerms(int index){
        return queryTerms.get(index);
    }
    public Set<Integer> queryLength(){
        return queryTerms.keySet();
    }
}
