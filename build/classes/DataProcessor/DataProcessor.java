package dataprocessor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.PostingsEnum;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.util.BytesRef;
/**
 *
 * @author pavanjoshi
 */

/*
Class Name : DataProcessor
Arguments : indexPath(String)
Purpose :   Process the given Index Path to extract terms and postings list
Description :
             Class to read the index files and store the postings lists mapped to the respective dictionary term
in a HashMap by extracting the data from the index using lucene core. postings list is implemented using a linked 
list defined by PostingsList and Postings in the same package
Dependencies : PostingsList, Postings
*/
public class DataProcessor {

	private String indexPath,outputFilePath,inputFilePath;
        IndexReader reader;
        Map<String,PostingsList> postingsLists;
        /*
        Type : Constructor
        Purpose : Process the index files and create the dictionary and postings list
        Description : 
                        Constructor calls the functionalities to perform data processing to extract dictionary
        and postings list after initializing the respective components that are necessary
        */
	public DataProcessor(String indexPath){
		try {

			this.indexPath = indexPath;

			this.reader = DirectoryReader.open(FSDirectory.open(new File(indexPath).toPath()));
                        this.postingsLists = new HashMap<>();
                        Object[] fields = MultiFields.getIndexedFields(this.reader).toArray();
                        String indexFields[] = new String[fields.length-2];
                        int index = 0;
                        for (Object field : fields)
                            if (field.toString().contains("text_")){
                                indexFields[index++] = field.toString();
                            }
                        Map dictionary = listTerms(indexFields);
                        for(Object key : dictionary.keySet()){
                            PostingsList termPostings = postingsListCreator((String)key,(ArrayList<String>)dictionary.get(key));
                            this.postingsLists.put((String)key, termPostings);
                        }
                        
		}
		catch (ArrayIndexOutOfBoundsException | IOException e){
			System.out.println(e);
		}
	}
        /*
        Function Name : listTerms
        Purpose : Extract the dictionary given the field name
        Description :
                Method takes the field names as input and extracts the terms from the index using these field names
        to create the term dictionary
        */
        private Map listTerms(String[] indexFields) throws IOException{
            Map dictionary = new HashMap();
            for (String field : indexFields) {
                TermsEnum terms = MultiFields.getTerms(this.reader,field).iterator();
                while(terms.next()!=null){
                    String term = terms.term().utf8ToString();
                    if(dictionary.containsKey(term)){
                        ArrayList<String> value = (ArrayList<String>) dictionary.get(term);
                        value.add(field);
                        dictionary.put(term, value);
                    }
                    else {
                        ArrayList<String> arrayList = new ArrayList<>(3);
                        arrayList.add(field);
                        dictionary.put(term, arrayList);
                    }
                }
            }
            return dictionary;
        }
        
        /*
        Function Name : postingsListCreator
        Purpose : Extract the postings list given the term and field name
        Description :
                Method takes the field names and term as input and extracts the postings from the index using these field names
        to create the postings list
        */
        private PostingsList postingsListCreator(String term, ArrayList<String> lang) throws IOException{
            Postings posting = null;
            PostingsList postingsList = null; 
            byte[] byteTerm = term.getBytes();
            for(String language : lang){
                PostingsEnum postingsEnum = MultiFields.getTermDocsEnum(this.reader,language, new BytesRef(byteTerm));
                while(postingsEnum.nextDoc()!=PostingsEnum.NO_MORE_DOCS){
                    posting = new Postings(postingsEnum.docID());
                    if(postingsList == null){
                        postingsList = new PostingsList(posting);
                    }
                    else {
                        postingsList.appendPostings(posting);
                    }
                }
            }
            if(postingsList!=null){
                postingsList.setSkipPointers();
            }
            return postingsList;
        }
        public int getDocCount(){
            return reader.numDocs();
        }
        public Map<String,PostingsList> getPostingsList(){
            return this.postingsLists;
        }
}

