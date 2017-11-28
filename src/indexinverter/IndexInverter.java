/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package indexinverter;

import dataprocessor.DataProcessor;
import dataprocessor.PostingsList;
import filemanager.InputManager;
import filemanager.OutputManager;
import java.util.Map;
import queryprocessor.DocumentAtATime;
import queryprocessor.GetPostings;
import queryprocessor.TermAtATime;

/**
 *
 * @author pavanjoshi
 */
public class IndexInverter {

    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) {
        // TODO code application logic here
        
        DataProcessor dataProcessor = new DataProcessor(args[0]);
        Map<String,PostingsList> postingsListDict = dataProcessor.getPostingsList();
        InputManager inputManager = new InputManager();
        if(!OutputManager.prepareOutputFile(args[1])){             
            System.out.println("Error : Output File could not be created");
        }
        if(inputManager.readFile(args[2])){
            for(int index : inputManager.queryLength()){
                String[] queryTerms = inputManager.queryTerms(index);
                GetPostings postings = new GetPostings(queryTerms,postingsListDict);
                TermAtATime termAtATime = new TermAtATime(queryTerms,postingsListDict);
                DocumentAtATime documentAtATime = new DocumentAtATime(queryTerms,postingsListDict);
            }
        }
        else {
            System.out.println("Error : Incorrect Input File Path");
        }
        OutputManager.closeFile();
    }
    
}
