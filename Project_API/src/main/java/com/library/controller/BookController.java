package com.library.controller;
///////////////////////////////////////////////////////////////////////////////////////////////////////
//  FILE : BookController.java
//  AUTHOR : Pranav Sehgal <PranavSehgalCS>
//
//  DESCRIPTION: Is a Controller file used to communicate with frontend
///////////////////////////////////////////////////////////////////////////////////////////////////////
import java.util.logging.Level;
import java.util.logging.Logger;
///////////////////////////////////////////////////////////////////////////////////////////////////////
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
///////////////////////////////////////////////////////////////////////////////////////////////////////
import com.library.model.Book;
import com.library.persistance.Books.BookDAO;
///////////////////////////////////////////////////////////////////////////////////////////////////////
@RestController
@RequestMapping("books")
public class BookController {
    private BookDAO bookDao;
    private static final Logger LOG = Logger.getLogger(BookController.class.getName());
    public BookController(BookDAO bookDao) {
        this.bookDao = bookDao;
    }
    
    @GetMapping("")
    public ResponseEntity<Book[]>getBookByName( @RequestParam(name = "bookId", required = true) int bookId){
        LOG.info("\nGET /book/"+bookId);
        try {
            Book[] retVal = this.bookDao.getBookById(bookId);
            if(retVal.length==0){
                if(bookId==-1){
                    return new ResponseEntity<Book[]>(retVal,HttpStatus.NOT_FOUND);
                }
                return new ResponseEntity<Book[]>(retVal,HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<Book[]>(retVal,HttpStatus.OK);
        } catch (Exception e) {
            LOG.log(Level.SEVERE,"ERROR At Controller While Getting --> "+e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/filter")
    public ResponseEntity<Book[]>getBookByFilter(   @RequestParam(name = "bookAuth", required = true) String bookAuth, 
                                                    @RequestParam(name = "ageRange", required = true) int[] ageRange,
                                                    @RequestParam(name = "bookTags", required = true) String[] bookTags){

        LOG.info("\nGET /book(filter)");
        try {
            Book[] responseVal = bookDao.getBooksByFilter(bookAuth, ageRange, bookTags);
            if(responseVal==null){
                System.out.println("SENDING NULL :(");
                Book retBook = new Book(0, "NULL", "NULL", ageRange, bookTags);
                Book[] retVal = new Book[1];
                retVal[0] = retBook;
                return new ResponseEntity<Book[]>(retVal ,HttpStatus.NOT_FOUND);
            }else{
                System.out.println("SENDING ARRAY of Size "+responseVal.length+ ":(");
                return new ResponseEntity<Book[]>(responseVal,HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
///////////////////////////////////////////////////////////////////////////////////////////////////////
    @PostMapping("")
    public ResponseEntity<Boolean> createBook(  @RequestParam(name = "bookName", required = true) String bookName, 
                                                @RequestParam(name = "bookAuth", required = true) String bookAuth,
                                                @RequestParam(name = "ageRange", required = true) int[] ageRange,
                                                @RequestParam(name = "bookTags", required = true) String[] tags
                                                ){
        LOG.info("\nPOST /Book/" + bookName );
        try{
            Boolean responseVal = bookDao.createBook(bookName, bookAuth, ageRange, tags);
            if(responseVal){
                return new ResponseEntity<Boolean>(responseVal, HttpStatus.CREATED);
            }
            else{
                return new ResponseEntity<Boolean>(false, HttpStatus.CONFLICT);
            }
        }
        catch(Exception e){
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
///////////////////////////////////////////////////////////////////////////////////////////////////////
    @PutMapping("")
    public ResponseEntity<Boolean> updateBook(  @RequestParam(name = "bookId", required = true) int bookId, 
                                                @RequestParam(name = "bookName", required = true) String bookName, 
                                                @RequestParam(name = "bookAuth", required = true) String bookAuth,
                                                @RequestParam(name = "ageRange", required = true) int[] ageRange,
                                                @RequestParam(name = "bookTags", required = true) String[] tags
                                                ){
        LOG.info("\nPUT /book/" + bookId);
        try{
            Boolean reponseVal =  bookDao.updateBook(bookId, bookName, bookAuth, ageRange, tags);
            if(reponseVal){
                return new ResponseEntity<Boolean>(reponseVal, HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        }
        catch(Exception e){
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
///////////////////////////////////////////////////////////////////////////////////////////////////////
    @DeleteMapping("")
    public ResponseEntity<Boolean> deleteBook(  @RequestParam(name = "bookId", required = true) int bookId){
        LOG.info("\nDELETE /book/" + bookId);
        try {
            Boolean reponseVal =  bookDao.deleteBook(bookId);
            if(reponseVal){
                return new ResponseEntity<Boolean>(reponseVal, HttpStatus.OK);
            }
            else{
                return new ResponseEntity<Boolean>(reponseVal, HttpStatus.NOT_FOUND);
            }
        } catch(Exception e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}