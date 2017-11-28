/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package queryprocessor;

import dataprocessor.Postings;
import dataprocessor.PostingsList;
import filemanager.OutputManager;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author pavanjoshi
 */

/*
Description : 
    Term-at-a-time implementation of or & and operations over the query terms. The algorithm is an array implementation
of the Term-at-a-time scoring algorithm with the term score incremented each time being 1. The array is implemented
using a HashMap for memory efficiency. Each input operation over the HashMap is considered as a comparision.
*/
public class TermAtATime {
    private String[] queryTerms;
    private Map<String,PostingsList> termPostings;
    private Map<Integer,Integer> resultDocList;
    public TermAtATime(String[] queryTerms,Map<String,PostingsList> postingsList){
        this.queryTerms = queryTerms;
        termPostings = new HashMap<>();
        for(String term : queryTerms){
            termPostings.put(term, (PostingsList) postingsList.get(term));
        }   
        resultDocList = new TreeMap<>();
        this.and();
        resultDocList.clear();
        this.or();
    }
    private void or(){
        OutputManager.writeOutput("TaatOr\n");
        for(String term : queryTerms){
            Postings postings = null;
            if(termPostings.get(term)!=null)
                postings = termPostings.get(term).getFirst();
            while(postings!=null){
                if(resultDocList.containsKey(postings.getDocID()))
                    resultDocList.put(postings.getDocID(),resultDocList.get(postings.getDocID())+1);
                else
                    resultDocList.put(postings.getDocID(),1);
                postings = postings.getNext();
            }
            OutputManager.writeOutput(term+" ");
        }
        OutputManager.writeOutput("\nResults: ");
        int docCount = 0, comparisions = 0;
        for(int docID: resultDocList.keySet()){
            if(resultDocList.get(docID)>0){
                OutputManager.writeOutput((docID)+" ");
                docCount++;
            }
            if(resultDocList.get(docID)>0)
                comparisions += resultDocList.get(docID);
        }
        OutputManager.writeOutput("\nNumber of documents in results: "+docCount);
        OutputManager.writeOutput("\nNumber of comparisons: "+comparisions);
        OutputManager.writeOutput("\n");
    }
    private void and(){
        OutputManager.writeOutput("TaatAnd\n");
        for(String term : queryTerms){
            Postings postings = null;
            if(termPostings.get(term)!=null)
                postings = termPostings.get(term).getFirst();
            while(postings!=null){
                if(resultDocList.containsKey(postings.getDocID()))
                    resultDocList.put(postings.getDocID(),resultDocList.get(postings.getDocID())+1);
                else
                    resultDocList.put(postings.getDocID(),1);
                postings = postings.getNext();
            }
            OutputManager.writeOutput(term+" ");
        }
        OutputManager.writeOutput("\nResults: ");
        int docCount = 0, comparisions = 0;
        if(this.queryTerms.length>1){
            for(int docID : resultDocList.keySet()){
                if(resultDocList.get(docID)==queryTerms.length){
                    OutputManager.writeOutput((docID)+" ");
                    docCount++;
                }
                if(resultDocList.get(docID)>0)
                    comparisions += resultDocList.get(docID);
            }
        }
        OutputManager.writeOutput("\nNumber of documents in results: "+docCount);
        OutputManager.writeOutput("\nNumber of comparisons: "+comparisions);
        OutputManager.writeOutput("\n");
        
        
    }
}
