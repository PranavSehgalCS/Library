package com.library.controller;
///////////////////////////////////////////////////////////////////////////////////////////////////////
//  FILE : BorrowController.java
//  AUTHOR : Pranav Sehgal <PranavSehgalCS>
//
//  DESCRIPTION: Is a Controller file with preconfigurations all set.
//               USED to take requests [GET, POST, DELETE] and send back data
///////////////////////////////////////////////////////////////////////////////////////////////////////
import java.util.logging.Level;
import java.util.logging.Logger;
///////////////////////////////////////////////////////////////////////////////////////////////////////
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
///////////////////////////////////////////////////////////////////////////////////////////////////////
import com.library.model.Tag;
import com.library.model.Borrow;
import com.library.persistance.Borrow.BorrowDAO;
///////////////////////////////////////////////////////////////////////////////////////////////////////
@RestController
@RequestMapping("borrows")
public class BorrowController {
    private BorrowDAO borrowDAO;
    private static final Logger LOG = Logger.getLogger(BorrowController.class.getName());

    public BorrowController(BorrowDAO borrowDAO){
        this.borrowDAO = borrowDAO;
    }
    @GetMapping("")
    public ResponseEntity<Tag[]>getBorrowers( ){
    LOG.info("\nGET /borrowers");
        try {
            Tag[] retVal = this.borrowDAO.getBorrowers();
            return new ResponseEntity<Tag[]>(retVal,HttpStatus.OK);
        } catch (Exception e) {
            LOG.log(Level.SEVERE,"ERROR At Controller While Getting --> "+e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{accName}")
    public ResponseEntity<Borrow[]>getMyBorrows(  @PathVariable String accName){
        LOG.info("\nGET /MyBorrows/"+accName);
        try {
            Borrow[] retVal = this.borrowDAO.getMyBorrows(accName);
            if(retVal!=null){
                return new ResponseEntity<Borrow[]>(retVal ,HttpStatus.OK);
            }else{
                
                return new ResponseEntity<Borrow[]>(HttpStatus.CONFLICT);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("")
    public ResponseEntity<Boolean> createBorrow(@RequestParam(name = "accName", required = true) String accName, 
                                                @RequestParam(name = "borrDate", required = true) String borrDate,
                                                @RequestParam(name = "bookId", required = true) Integer bookId, 
                                                @RequestParam(name = "bookName", required = true) String bookName
                                                ){
        LOG.info("\nPOST /borrow/" + accName+"-->"+bookName);
        try{
            Boolean responseVal = borrowDAO.createBorrow(accName, borrDate, bookId, bookName);
            if(responseVal){
                return new ResponseEntity<Boolean>(true, HttpStatus.CREATED);
            }
            else{
                return new ResponseEntity<Boolean>(false, HttpStatus.CREATED);
            }
        }
        catch(Exception e){
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("")
    public ResponseEntity<Boolean> deleteBorrow(@RequestParam(name = "accName", required = true) String accName,
                                                @RequestParam(name = "bookId" , required = true) Integer bookId
                                                    ){
        LOG.info("\nDELETE /borrow/" + accName +"-->"+bookId);
        try {
            Boolean reponseVal =  borrowDAO.deleteBorrow(accName, bookId);
            if(reponseVal){
                return new ResponseEntity<Boolean>(reponseVal, HttpStatus.OK);
            }else{
                return new ResponseEntity<Boolean>(reponseVal, HttpStatus.NOT_FOUND);
            }
        } catch(Exception e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
