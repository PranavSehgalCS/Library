package com.library.controller;
///////////////////////////////////////////////////////////////////////////////////////////////////////
//  FILE : AccountController.java
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.library.model.Account;
import com.library.persistance.Account.AccountDAO;
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@RestController
@RequestMapping("accounts")

public class AccountController {
    private  AccountDAO accountDao;
    private static final Logger LOG = Logger.getLogger(AccountController.class.getName());
    public AccountController(AccountDAO accountDao) {
        this.accountDao = accountDao;
    }
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @GetMapping("")
    public ResponseEntity<Account>getAccount(  @RequestParam(name = "accName", required = true)String accName,
                                                    @RequestParam(name = "accPass", required = true)String accPass){
        LOG.info("\nGET /Account/"+accName);
        try {
            Account retVal = this.accountDao.getAccount(accName, accPass);
            if(retVal!=null){
                return new ResponseEntity<Account>(retVal ,HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @PostMapping("")
    public ResponseEntity<Integer> createAccount(   @RequestParam(name = "accName", required = true) String accName, 
                                                    @RequestParam(name = "accPass", required = true) String accPass
                                                ){
        LOG.info("\nPOST /Account/" + accName );
        try{
            int responseVal = accountDao.createAccount(accName,accPass);
            if(responseVal==0){
                return new ResponseEntity<Integer>(responseVal, HttpStatus.CREATED);
            }
            else{
                return new ResponseEntity<Integer>(responseVal, HttpStatus.CONFLICT);
            }
        }
        catch(Exception e){
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @PutMapping("")
    public ResponseEntity<Boolean> updateAccount(   @RequestParam(name = "accId", required = true) Integer accId,
                                                    @RequestParam(name = "accName", required = true) String  accName, 
                                                    @RequestParam(name = "accPass", required = true) String  accPass
                                                ){
        LOG.info("\nPUT /Account/" + accId + ":" + accName);
        try{
            Boolean reponseVal = accountDao.updateAccount(accId, accName, accPass);
            return new ResponseEntity<Boolean>(reponseVal, HttpStatus.OK);
        }
        catch(Exception e){
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @DeleteMapping("")
    public ResponseEntity<Boolean> deleteTemplate(  @RequestParam(name = "accId", required = true) Integer accId,
                                                    @RequestParam(name = "accPass", required = true) String accPass
                                                    ){
        LOG.info("\nDELETE /template/" + accId);
        try {
            Boolean reponseVal =  accountDao.deleteAccount(accId, accPass);
            if(reponseVal){
                return new ResponseEntity<Boolean>(reponseVal, HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch(Exception e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}
