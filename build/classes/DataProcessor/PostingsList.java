/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataprocessor;

import filemanager.OutputManager;
import static java.lang.Math.round;
import static java.lang.Math.sqrt;

/**
 *
 * @author pavanjoshi
 */
public class PostingsList{
    private int length;
    private Postings begin,end;
    
    public PostingsList(Postings first){
        this.begin = first;
        this.end = first;
        this.length = 1;
    }
    public Integer getLength(Boolean check){
        return this.length;
    }
    public Postings getFirst(){
        return this.begin;
    }
    public void appendPostings(Postings posting){
        this.end.setNext(posting); 
        this.end = posting;
        this.length += 1;
    }
    public void printPostings(){
        Postings posting = this.begin;
        while(posting!=null){
            OutputManager.writeOutput(posting.getDocID()+" ");
            posting = posting.getNext();
        }
    }
    public void setSkipPointers(){
        int count = 1;
        Postings current = this.begin;
        Postings nextPosting = this.begin.getNext();
        int skipLength = (int) round(sqrt(this.length));
        if(skipLength>1){
            while(nextPosting.getNext()!=null){
                nextPosting = nextPosting.getNext();
                count++;
                count = count % skipLength;
                if(count==0){
                    current.setSkipPointer(nextPosting);
                    current = nextPosting;
                }
            }
            if(count>1){
                current.setSkipPointer(nextPosting);
            }
        }
    }
}
