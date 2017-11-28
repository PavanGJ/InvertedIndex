/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataprocessor;

/**
 *
 * @author pavanjoshi
 */
public class Postings {
    
    private int docID;
    private Postings next,skip;
    
    public Postings(int docID){
        this.docID = docID;
        this.skip = null;
        this.next = null;
    }
    public Postings getNext(){
        return this.next;
    }
    public Postings getSkipPointer(){
        return this.skip;
    }
    public int getDocID(){
        return this.docID;
    }
    public void setNext(Postings next){
        this.next = next;
        
    }
    public void setSkipPointer(Postings skip){
        this.skip = skip;
    }
}
