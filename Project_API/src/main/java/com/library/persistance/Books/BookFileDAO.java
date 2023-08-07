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
import com.library.persistance.Borrow.BorrowFileDAO;
import com.library.model.Book;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;


@Component
public class BookFileDAO implements BookDAO{
    private static int nextID;
    private static Commonfuncs comm;
    private Boolean updated = false;
    private static Map<Integer, Book> BookHolder;

    public BookFileDAO( @Value("${spring.datasource.url}") String database,
                        @Value("${spring.datasource.username}") String datauser,
                        @Value("${spring.datasource.password}") String datapass){
        try {
            BookFileDAO.BookHolder = new HashMap<Integer, Book>();
            BookFileDAO.comm = new Commonfuncs(database, datauser, datapass);
            this.updated = loadBooks();
        } catch (Exception e) {
            System.out.println("\nERROR While Initializing Connection --> " + e);
        }
        
    }
    public static boolean loadBooks() throws RuntimeException{
        try {
            BookHolder.clear();
            Book mapObj = new Book(0, null, null, null, null);
            ResultSet load = comm.getQuery("SELECT * FROM books ORDER BY bookid;");
            while(load.next()){
                mapObj = new Book(load.getInt("bookId"),
                                load.getString("bookName"),
                                load.getString("bookAuth"),
                                new int[] {load.getInt("minAge"),load.getInt("maxAge")},
                                getTags(load.getInt("bookId")));
                BookHolder.put(mapObj.getBookId(), mapObj);
            }
            nextID = mapObj.getBookId()+1;
            load.getStatement().getConnection().close();
            return true;
        } catch (Exception e) {
            System.out.println("\nERROR at loadBooks(BookFileDAO) --> " + e);
            return false;
        }
    }
    private static String[] getTags(int bookId){
        try {
            ResultSet load = BookFileDAO.comm.getQuery("SELECT tag FROM tags WHERE bookId = " + bookId +";");
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
    private boolean matchTag(String a, String[] b){
        if(a.equals("null")){
            return true;
        }else if(b==null){
            return false;
        }else if(b.length==0){
            return false;
        }
        for(String i:b){
            if(i.equals(a)){
                return true;
            }
        }
        return false;
    }
    
    @Override
    public Book[] getBookById(int bookId) throws RuntimeException{
        try {
            if(!updated){ updated = loadBooks(); }
            if(bookId!=-1){
                if(BookFileDAO.BookHolder.containsKey(bookId)){
                    Book[] retVal = new Book[1];
                    retVal[0] = BookFileDAO.BookHolder.get(bookId);
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
                if(matchTag(bookTags[0], mapObj.getBookTags())||bookTags.length==0){
                    if(auth.equals("null") || auth.equals(mapObj.getBookAuth())){
                        if(ageR == null || (ageR[0]<=mapObj.getAgeRange()[0] && ageR[1]>=mapObj.getAgeRange()[1])){
                            arr.add(mapObj);
                        }
                    }
                }
            }
            System.out.println("Filter Triggered With Array of Size : " + arr.size());
            if(arr.size()==0){
                arr.add(new Book(-1, auth, auth, ageR, bookTags));;
            }
            Book[] retVal = new Book[arr.size()];
            for(int i =0; i<arr.size();i++){
                retVal[i]=arr.get(i);
            }
            return retVal;
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
            BookFileDAO.comm.setQuery("INSERT INTO books VALUES("+nextID+','+comm.qot(bookName)+','+comm.qot(bookAuth)+c(ageRange)+");");
            for(String i:bookTags){
                BookFileDAO.comm.setQuery("INSERT INTO tags VALUES("+nextID+","+comm.qot(i)+")");
            }
            BookFileDAO.BookHolder.put(nextID, new Book(nextID, bookName, bookAuth, ageRange, bookTags));
            
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
            if(BookFileDAO.BookHolder.containsKey(bookId)){
                Book mapObj = BookHolder.get(bookId);
                if(bookName.equals(mapObj.getBookName())){
                    BookFileDAO.comm.setQuery("UPDATE books SET bookName = '"+bookName+"' WHERE bookId = "+bookId+";");
                }
                if(ageRange!=mapObj.getAgeRange()){
                    BookFileDAO.comm.setQuery("UPDATE books SET minAge = "+ageRange[0]+" WHERE bookId = "+bookId+";");
                    BookFileDAO.comm.setQuery("UPDATE books SET maxAge = "+ageRange[1]+" WHERE bookId = "+bookId+";");
                }
                if(!bookAuth.equals(mapObj.getBookAuth())){
                    BookFileDAO.comm.setQuery("UPDATE books SET bookAuth = '"+ bookAuth +"' WHERE bookId = "+bookId+";");
                }
                BookFileDAO.comm.setQuery("DELETE FROM tags WHERE bookId = "+bookId+";");
                for(String i:bookTags){
                    mapObj.bookTags=bookTags;
                    BookFileDAO.comm.setQuery("INSERT INTO tags VALUES("+bookId+","+comm.qot(i)+")");
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
            boolean retVal = BookFileDAO.comm.setQuery("DELETE FROM books WHERE bookId = "+bookId+";");
            retVal = retVal && BookFileDAO.comm.setQuery("DELETE FROM tags WHERE bookId = "+bookId+";");
            retVal = retVal && BookFileDAO.comm.setQuery("DELETE FROM borrows WHERE bookId = "+bookId+";"); 
            if(retVal){
                BookHolder.remove(bookId);
            }
            BorrowFileDAO.loadBorrows();
            return retVal;
        }else{
            return false;
        }
    }
    
}
