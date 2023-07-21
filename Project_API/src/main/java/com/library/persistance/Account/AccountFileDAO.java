package com.library.persistance.Account;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.ResultSet;

import org.springframework.stereotype.Component;

import com.library.model.Account;
import com.library.persistance.Commonfuncs;

import org.springframework.beans.factory.annotation.Value;
///////////////////////////////////////////////////////////////////////////////////////////////////////
@Component
public class AccountFileDAO implements AccountDAO {
    private static int nextId;
    private Commonfuncs comm;

    public AccountFileDAO(  @Value("${spring.datasource.url}") String database ,
                            @Value("${spring.datasource.username}") String datauser,
                            @Value("${spring.datasource.password}") String datapass
                        ) throws IOException, SQLException {
        this.comm = new Commonfuncs(database, datauser, datapass);
        setNextId() ;
    }

    private boolean setNextId(){
        try {
            boolean retVal = false;
            ResultSet ress = this.comm.getQuery("SELECT MAX(accId) AS maid FROM accounts ");
            if(ress.next()){
                AccountFileDAO.nextId = (Integer)(ress.getInt("maid") + 1);
                if(AccountFileDAO.nextId>5){
                    retVal = true;
                }
            }
            ress.getStatement().getConnection().close();
            return retVal;
        } catch (Exception e) {
            System.out.println("ERROR at getNextId(AccountFileDAO) --> "+e);
            return false;
        }
    }
    
    
    /** 
     * @return Account with id :
     *              0 : No Such Account
     *              1 : Found Account, wrong password
     *              2 : No Connection to database
     */
    @Override
    public Account getAccount(String accName, String accPass){
        try {
            Account retval = null;
            ResultSet ress = comm.getQuery("SELECT * FROM accounts WHERE accName = "+comm.qot(accName.toLowerCase())+";");
            if(ress==null){
                retval = new Account(2, "", "", false);
            }else{
                if(!ress.next()){
                    retval = new Account(0, "", "", false);
                }else if(!ress.getString("accPass").equals(accPass)){
                    retval = new Account(1, "",  "", false);
                }else{
                    retval = new Account(   ress.getInt("accId"),
                                            ress.getString("accName").toLowerCase(),
                                            ress.getString("accPass"),
                                            ress.getBoolean("isAdmin")
                                        );
                }
                ress.getStatement().getConnection().close();
            }
            return retval;
        } catch (Exception e) {
            System.out.println("ERROR at getAccount(AccountFileDAO) --> "+e);
            return new Account(2, "", "", false);
        }
    }

    
    /** 
     *  0 : SUCCESS
     *  1 : name already exists
     *  2 : No Connection to database
     *  3 : MisAligned nextId
     *  4 : ERROR while adding
     */
    @Override
    public int createAccount(String accName, String accPass){
        Integer retVal = -1;
        try {
            retVal = 0;
            accName =accName.toLowerCase();
            ResultSet ress = comm.getQuery("SELECT accId FROM accounts WHERE accName = '" + accName.toLowerCase() +"';");
            if(ress == null){
                retVal = 2;
            }else if(ress.next()){
                retVal = 1;
            }else{
                if(nextId<5){
                    if(!setNextId()){
                        return 3;
                    }
                }
                if(!comm.setQuery("INSERT INTO accounts VALUES("+AccountFileDAO.nextId+", '"+ accName.toLowerCase()+"', '" + accPass +"' , false);")){
                    retVal = 4;
                }else{
                    AccountFileDAO.nextId++;
                }
            }
            if(ress!=null){
                ress.getStatement().getConnection().close();
            }
            return retVal;
            
        }catch (Exception e) {
            System.out.println("ERROR at createAccount(AccountFileDAO) --> "+e);
            return 4;
        }
    }


    
    /** 
     * 
     */
    @Override
    public boolean updateAccount(int accID, String accName, String accPass) {
        try {
            boolean retVal = true;
            ResultSet ress = comm.getQuery("SELECT * FROM accounts WHERE accId = "+accID+";");
            if(ress==null){
                retVal = false;
                System.out.println("T'was null");
            }else{
                if(!ress.next()){
                    retVal = false;
                    System.out.println("T'wasn't next");
                }else{
                    if(!ress.getString("accName").equals(accName)){
                        retVal = retVal && comm.setQuery(("UPDATE accounts SET accName = '" + accName.toLowerCase() + "' WHERE accId = '" + accID+"';"));
                    }
                    if(!ress.getString("accPass").equals(accPass)){
                        retVal = retVal && comm.setQuery(("UPDATE accounts SET accPass = '" + accPass + "' WHERE accId = '" + accID+"';"));
                    } 
                }
                ress.getStatement().getConnection().close();
            }
            return retVal;
        } catch (Exception e){
            System.out.println("ERROR at updateAccount(AccountFileDAO) --> "+e);
            return false;
        }
    }


    @Override
    public boolean deleteAccount(int accID, String accPass) {
        try {
            boolean retVal = true;
            ResultSet ress = comm.getQuery("SELECT accId FROM accounts WHERE accId = "+accID+" AND accPass = '"+accPass+"';");
            if(ress == null){
                retVal = false;
            }else{
                if(!ress.next()){
                    retVal = false;
                }else if(!comm.setQuery("DELETE FROM accounts WHERE accId = "+accID+";")){
                    retVal = false;
                }
                ress.getStatement().getConnection().close();
            }
            return retVal;
        } catch (Exception e) {
            return false;
        }
    }
    
}
