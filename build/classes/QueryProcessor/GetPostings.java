/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package queryprocessor;

import dataprocessor.PostingsList;
import filemanager.OutputManager;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author pavanjoshi
 */
public class GetPostings {
    private String[] queryTerms;
    public GetPostings(String[] queryTerms,Map<String,PostingsList> postingsList){
        this.queryTerms = queryTerms;
        for(String term : queryTerms){
            OutputManager.writeOutput("GetPostings\n"+term+"\nPostings list: ");
            if(postingsList.get(term)!=null)
                postingsList.get(term).printPostings();
            OutputManager.writeOutput("\n");
        }   
    }
}
