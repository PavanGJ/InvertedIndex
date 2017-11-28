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
import java.lang.NullPointerException;

/**
 *
 * @author pavanjoshi
 */

/*
Description : 
    Document-at-a-time parses through the postings list of the query terms one document at a time. 
*/
public class DocumentAtATime {
    private String[] queryTerms;
    private Map<String,PostingsList> termPostings;
    private Map<Integer,Integer> resultDocList;
    private Postings[] termPointers;
    public DocumentAtATime(String[] queryTerms,Map<String,PostingsList> postingsList){
        this.queryTerms = queryTerms;
        termPostings = new HashMap<>();
        termPointers = new Postings[queryTerms.length];
        int index = 0;
        for(String term : queryTerms){
            termPostings.put(term, (PostingsList) postingsList.get(term));
            if(termPostings.get(term)!=null)
                termPointers[index++] = termPostings.get(term).getFirst();
        }
        resultDocList = new TreeMap<>();
        this.and();
        index = 0;
        for(String term : queryTerms){
            termPostings.put(term, (PostingsList) postingsList.get(term));
            if(termPostings.get(term)!=null)
                termPointers[index++] = termPostings.get(term).getFirst();
        }
        resultDocList.clear();
        this.or();
    }
    private void or(){
        OutputManager.writeOutput("DaatOr\n");
        int comparisons = 0;
        int similarityCheck = 0;
        int pointerIndex = 0;
        for(String term : this.queryTerms){
            OutputManager.writeOutput(term+" ");
        }
        while(true){
            try{
                for(int index = 0; index < this.termPointers.length; index++){                    
                    if(this.termPointers.length==1){
                        while(this.termPointers[0]!=null){
                            this.resultDocList.put(this.termPointers[0].getDocID(), similarityCheck);
                            this.termPointers[0] = this.termPointers[0].getNext();
                        }                                                
                        throw new NullPointerException("OrOperationFinished");
                    }
                    if(index != pointerIndex){
                        if(this.termPointers[index].getDocID()>this.termPointers[pointerIndex].getDocID()){
                            this.resultDocList.put(this.termPointers[pointerIndex].getDocID(), similarityCheck);
                            this.termPointers[pointerIndex] = this.termPointers[pointerIndex].getNext();
                            pointerIndex = index;
                        }
                        else if(this.termPointers[index].getDocID()<this.termPointers[pointerIndex].getDocID()){
                            this.resultDocList.put(this.termPointers[index].getDocID(), similarityCheck);
                            this.termPointers[index] = this.termPointers[index].getNext();
                        }
                        else {
                            similarityCheck++;
                        }
                        comparisons++;
                    }
                }
                if(similarityCheck==this.termPointers.length-1){
                    resultDocList.put(this.termPointers[pointerIndex].getDocID(), similarityCheck);
                    for(int index = 0; index < this.termPointers.length; index++){
                        this.termPointers[index] = this.termPointers[index].getNext();
                    }
                    similarityCheck = 0;
                }
            }
            catch(NullPointerException npe){
                Postings[] intermediatePointers;
                intermediatePointers = new Postings[this.termPointers.length-1];
                for(int index = 0,i=0; index<this.termPointers.length; index++){
                    if(this.termPointers[index]!=null)
                        intermediatePointers[i++]=this.termPointers[index];
                }
                pointerIndex = 0;
                this.termPointers = intermediatePointers;
                if(this.termPointers.length==0){
                    break;
                }
            }
        }
        OutputManager.writeOutput("\nResults: ");
        int docCount = 0;
        for(int docID: resultDocList.keySet()){
            OutputManager.writeOutput((docID)+" ");
            docCount++;
        }
        OutputManager.writeOutput("\nNumber of documents in results: "+docCount);
        OutputManager.writeOutput("\nNumber of comparisons: "+comparisons); 
        OutputManager.writeOutput("\n");
    }
    private void and(){
        OutputManager.writeOutput("DaatAnd\n");
        int comparisons = 0;
        int similarityCheck = 0;
        int pointerIndex = 0;
        for(String term : this.queryTerms){
            OutputManager.writeOutput(term+" ");
        }
        while(true){
            try{
                for(int index = 0; index < this.termPointers.length; index++){                    
                    if(index != pointerIndex){                        
                        if(this.termPointers[index].getDocID()>this.termPointers[pointerIndex].getDocID()){
                            this.termPointers[pointerIndex] = this.termPointers[pointerIndex].getNext();
                            if(this.termPointers[pointerIndex].getDocID()<this.termPointers[index].getDocID())
                                pointerIndex = index;
                        }
                        else if(this.termPointers[index].getDocID()<this.termPointers[pointerIndex].getDocID()){
                            this.termPointers[index] = this.termPointers[index].getNext();
                        }
                        else {
                            similarityCheck++;
                        }
                        comparisons++;
                    }
                }
                if(similarityCheck==this.termPointers.length-1){
                    resultDocList.put(this.termPointers[pointerIndex].getDocID(), similarityCheck);
                    for(int index = 0; index < this.termPointers.length; index++){
                        this.termPointers[index] = this.termPointers[index].getNext();
                    }
                    similarityCheck = 0;
                }
            }
            catch(NullPointerException npe){
                break;
            }
        }
        OutputManager.writeOutput("\nResults: ");
        int docCount = 0;
        if(this.queryTerms.length>1){
            for(int docID: resultDocList.keySet()){
                OutputManager.writeOutput((docID)+" ");
                docCount++;
            }
        }
        OutputManager.writeOutput("\nNumber of documents in results: "+docCount);
        OutputManager.writeOutput("\nNumber of comparisons: "+comparisons);  
        OutputManager.writeOutput("\n");
    }
}
