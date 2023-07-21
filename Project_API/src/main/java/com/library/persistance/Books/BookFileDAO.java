package com.library.persistance.Books;
///////////////////////////////////////////////////////////////////////////////////////////////////////
//  FILE : TemplateFileDAO.java
//  AUTHOR : Pranav Sehgal <PranavSehgalCS>
//  DESCRIPTION: IMPLEMENTS BookDAO interface
//               VALUES are taken from the src/main//resources/application.properties file
//
///////////////////////////////////////////////////////////////////////////////////////////////////////
import java.util.Map;

import java.util.ArrayList;
import java.util.HashMap;

import java.sql.ResultSet;
import com.library.persistance.Commonfuncs;

import com.library.model.Book;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;


@Component
public class BookFileDAO implements BookDAO{
    private int nextID;
    private Commonfuncs comm;
    private Boolean updated = false;
    private Map<Integer, Book> BookHolder;

    public BookFileDAO( @Value("${spring.datasource.url}") String database,
                        @Value("${spring.datasource.username}") String datauser,
                        @Value("${spring.datasource.password}") String datapass){
        try {
            this.BookHolder = new HashMap<Integer, Book>();
            this.comm = new Commonfuncs(database, datauser, datapass);
            this.updated = loadBooks();
        } catch (Exception e) {
            System.out.println("\nERROR While Initializing Connection --> " + e);
        }
        
    }
    private boolean loadBooks() throws RuntimeException{
        try {
            this.BookHolder.clear();
            Book mapObj = new Book(0, null, null, null, null);
            ResultSet load = this.comm.getQuery("SELECT * FROM books ORDER BY bookid;");
            while(load.next()){
                mapObj = new Book(load.getInt("bookId"),
                                load.getString("bookName"),
                                load.getString("bookAuth"),
                                new int[] {load.getInt("minAge"),load.getInt("maxAge")},
                                getTags(load.getInt("bookId")));
                BookHolder.put(mapObj.getBookId(), mapObj);
            }
            this.nextID = mapObj.getBookId()+1;
            load.getStatement().getConnection().close();
            return true;
        } catch (Exception e) {
            System.out.println("\nERROR at loadBooks(BookFileDAO) --> " + e);
            return false;
        }
    }
    private String[] getTags(int bookId){
        try {
            ResultSet load = this.comm.getQuery("SELECT tag FROM tags WHERE bookId = " + bookId +";");
            ArrayList<String> arr = new ArrayList<String>();
            while(load.next()){
                arr.add(load.getString("tag"));
            }
            load.getStatement().getConnection().close(); 
            if(arr.size()>0){
                int index = 0;
                String[] retVal =  new String[arr.size()];
                for(String i: arr){
                    retVal[index] = i;
                    index++;
                }
                return retVal;
            }
            return new String[0];
        } catch (Exception e) {
            System.out.println("\nERROR at getTags(BookFileDAO) --> " + e);
            throw new RuntimeException();
        } 
    }
    private boolean matchTag(String[] a, String[] b){
        if(a==null || b==null){
            return false;
        }else if(a.length==0 || b.length==0){
            return false;
        }
        Map<String,Integer> mapper = new HashMap<String,Integer>();
        for(String i:a){ mapper.put(i, 0); }
        for(String i:b){
            if(mapper.containsKey(i)){ return true; }
        }
        return false;
    }
    
    @Override
    public Book[] getBookById(int bookId) throws RuntimeException{
        try {
            if(!updated){ updated = loadBooks(); }
            if(bookId!=-1){
                if(this.BookHolder.containsKey(bookId)){
                    Book[] retVal = new Book[1];
                    retVal[0] = this.BookHolder.get(bookId);
                    retVal[0].bookTags = getTags(bookId);
                    return retVal;
                }else{
                    return new Book[0];
                }
            }else{
                if(BookHolder.size()==0){
                    return new Book[0];
                }
                int index = 0;
                Book[] retVal = new Book[BookHolder.size()];
                for(int i:BookHolder.keySet()){
                    retVal[index] = BookHolder.get(i);
                    index++;
                }
                return retVal;
            }
        }catch (Exception e) {
            System.out.println("\nERROR at getBookById(BookFileDAO) --> " + e);
            throw new RuntimeException();
        }
    }

    @Override
    public Book[] getBooksByFilter(String auth, int[] ageR, String[] bookTags) throws RuntimeException {
        try{
            Book mapObj;
            ArrayList<Book> arr = new ArrayList<Book>();
            for(int i: BookHolder.keySet()){
                mapObj = BookHolder.get(i);
                if(matchTag(bookTags, mapObj.getBookTags())||bookTags.length==0){
                    if(auth.equals("null") || auth.equals(mapObj.getBookAuth())){
                        if(ageR == null || (ageR[0]<=mapObj.getAgeRange()[0] && ageR[1]>=mapObj.getAgeRange()[1])){
                            arr.add(mapObj);
                        }
                    }
                }
            }
            if(arr.size()==0){
                return null;
            }else{
                Book[] retVal = new Book[arr.size()];
                for(int i =0; i<arr.size();i++){
                    retVal[i]=arr.get(i);
                }
                return retVal;
            }
        }catch(Exception e){
            System.out.println("\nERROR at getBookByFilter(BookFileDAO) --> " + e);
            throw new RuntimeException();
        }
    }

    String c(int[] i){
        return " ," + i[0] +", " + i[1];
    }
    @Override
    public boolean createBook(String bookName, String bookAuth,int[] ageRange, String[] bookTags) throws RuntimeException {
        try {
            if(ageRange == null){
                ageRange = new int[]{0,100};
            }
            this.comm.setQuery("INSERT INTO books VALUES("+nextID+','+comm.qot(bookName)+','+comm.qot(bookAuth)+c(ageRange)+");");
            for(String i:bookTags){
                this.comm.setQuery("INSERT INTO tags VALUES("+nextID+","+comm.qot(i)+")");
            }
            this.BookHolder.put(nextID, new Book(nextID, bookName, bookAuth, ageRange, bookTags));
            
            nextID++;
            return true;
        } catch (Exception e) {
            System.out.println("\nERROR at getBookById(BookFileDAO) --> " + e);
            throw new RuntimeException();
        }
    }

    @Override
    public boolean updateBook(int bookId, String bookName, String bookAuth, int[] ageRange, String[] bookTags) throws RuntimeException {
        try {
            if(this.BookHolder.containsKey(bookId)){
                Book mapObj = BookHolder.get(bookId);
                if(bookName.equals(mapObj.getBookName())){
                    this.comm.setQuery("UPDATE books SET bookName = '"+bookName+"' WHERE bookId = "+bookId+";");
                }
                if(ageRange!=mapObj.getAgeRange()){
                    this.comm.setQuery("UPDATE books SET minAge = "+ageRange[0]+" WHERE bookId = "+bookId+";");
                    this.comm.setQuery("UPDATE books SET maxAge = "+ageRange[1]+" WHERE bookId = "+bookId+";");
                }
                if(!bookAuth.equals(mapObj.getBookAuth())){
                    this.comm.setQuery("UPDATE books SET bookAuth = '"+ bookAuth +"' WHERE bookId = "+bookId+";");
                }
                this.comm.setQuery("DELETE FROM tags WHERE"+bookId+";");
                for(String i:bookTags){
                    mapObj.bookTags=bookTags;
                    this.comm.setQuery("INSERT INTO tags VALUES("+bookId+","+comm.qot(i)+")");
                }
                BookHolder.put(bookId, new Book(bookId, bookName, bookAuth, ageRange, bookTags));
                return true;
            }else{
                return false;
            }
        } catch (Exception e) {
            System.out.println("\nERROR at updateBook(BookFileDAO) --> " + e);
            throw new RuntimeException();
        }
    }

    @Override
    public boolean deleteBook(int bookId) throws RuntimeException {
        if(BookHolder.containsKey(bookId)){
            boolean retVal = this.comm.setQuery("DELETE FROM books WHERE bookId = "+bookId+";");
            retVal = retVal && this.comm.setQuery("DELETE FROM tags WHERE bookId = "+bookId+";");
            if(retVal){
                BookHolder.remove(bookId);
            }
            return retVal;
        }else{
            return false;
        }
    }
    
}
