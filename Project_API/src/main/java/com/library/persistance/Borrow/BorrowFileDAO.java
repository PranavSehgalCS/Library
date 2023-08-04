package com.library.persistance.Borrow;
///////////////////////////////////////////////////////////////////////////////////////////////////////
//  FILE : BorrowFileDAO.java
//  AUTHOR : Pranav Sehgal <PranavSehgalCS>
//  DESCRIPTION: IMPLEMENTS BorrowDAO interface and declares defined functions
///////////////////////////////////////////////////////////////////////////////////////////////////////
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

import java.sql.ResultSet;

import com.library.model.Tag;
import com.library.model.Borrow;
import com.library.persistance.Commonfuncs;
import org.springframework.stereotype.Component;

import org.springframework.beans.factory.annotation.Value;
///////////////////////////////////////////////////////////////////////////////////////////////////////

@Component
public class BorrowFileDAO implements BorrowDAO{
    private Commonfuncs comm;
    private ArrayList<Tag> borrowers = new ArrayList<Tag>();
    private Map<String,ArrayList<Borrow>> BorrHolder = new HashMap<String,ArrayList<Borrow>>();
    public BorrowFileDAO(   @Value("${spring.datasource.url}") String database,
                            @Value("${spring.datasource.username}") String datauser,
                            @Value("${spring.datasource.password}") String datapass
    ){
        this.comm = new Commonfuncs(database, datauser, datapass);
        loadBorrows();
    }

    public boolean loadBorrows() throws RuntimeException{
        try {
            borrowers.clear();
            BorrHolder.clear();
            ArrayList<Borrow> temp;
            Borrow mapObj = new Borrow("","",0,"");
            ResultSet load = comm.getQuery("SELECT * FROM borrows");
            while(load.next()){
                mapObj = new Borrow(load.getString("accName"),
                                    load.getString("borrDate"),
                                    load.getInt(   "bookId"  ),
                                    load.getString("bookName"));
                if(BorrHolder.containsKey(mapObj.getAccName())){
                    temp = BorrHolder.get(mapObj.getAccName());
                    temp.add(mapObj);
                }else{
                    temp = new ArrayList<Borrow>();
                    temp.add(mapObj);
                    BorrHolder.put(mapObj.getAccName(), temp);
                }
            }
            if(!BorrHolder.isEmpty()){
                for(String i:BorrHolder.keySet()){
                    borrowers.add(new Tag(BorrHolder.get(i).size(),i));
                }
            }
            load.getStatement().getConnection().close();
            return true;
        } catch (Exception e) {
            System.out.println("\nERROR at loadBorrows(BorrowFileDAO) --> " + e);
            return false;
        }
    }
    
    @Override
    public Tag[] getBorrowers(){
        try {
            Tag[] retVal = new Tag[borrowers.size()];
            for(int i =0; i<retVal.length; i++){
                retVal[i] = borrowers.get(i);
            }
            return retVal;
        } catch (Exception e) {
            System.out.println("\nERROR at getBorrowers(BorrowFileDAO) --> " + e);
            return null;
        }
    }

    @Override
    public Borrow[] getMyBorrows(String accName) {
        try {
            if(!BorrHolder.containsKey(accName)){
                return null;
            }else{
                ArrayList<Borrow> borrList = BorrHolder.get(accName);
                Borrow[] retVal = new Borrow[borrList.size()];
                for(int i = 0; i< borrList.size(); i++){
                    retVal[i] = borrList.get(i);
                }
                return retVal;
            }
        } catch (Exception e) {
            System.out.println("\nERROR at getMyBorrows(BorrowFileDAO) --> " + e);
            return null;
        }
    }

    @Override
    public Boolean createBorrow(String accName, String borrDate, int bookId, String bookName) {
        try {
            Borrow newBor = new Borrow(accName, borrDate, bookId, bookName);
            if(this.BorrHolder.containsKey(accName)){
                for(Borrow i:this.BorrHolder.get(accName)){
                    if(i.getBookId()==bookId){
                        System.out.println("False Cuz Book Exists"); 
                        return false;
                    }
                }
            }
            if(this.comm.setQuery("INSERT INTO borrows VALUES("+bookId + ", '" + borrDate+"', '"+accName+"', '"+bookName+"');")){
                if(!this.BorrHolder.containsKey(accName)){
                    this.BorrHolder.put(accName, new ArrayList<Borrow>());
                    borrowers.add(new Tag(0, accName));
                }
                for(int i = 0; i<borrowers.size();i++){
                    if(borrowers.get(i).getTagName().equals(accName)){
                        borrowers.set(i, new Tag(borrowers.get(i).getTagId()+1,accName));
                    }
                }
                this.BorrHolder.get(accName).add(newBor);
                return true;
            }
            System.out.println("False Cuz SQL"); 
            return false;
        } catch (Exception e) {
            System.out.println("False Cuz ERROR"); 
            return false;
        }
    }

    @Override
    public Boolean deleteBorrow(String accName, int bookId) {
        try {
            if(this.comm.setQuery("DELETE FROM borrows WHERE accName = '"+accName+"' AND bookId = "+bookId+";")){
                if(this.BorrHolder.containsKey(accName)){
                    ArrayList<Borrow> temp = new ArrayList<Borrow>();
                    for(Borrow i:this.BorrHolder.get(accName)){
                        if(i.getBookId()!=bookId){
                            temp.add(i);
                        }
                    }
                    for(int i = 0; i<borrowers.size();i++){
                        if(borrowers.get(i).getTagName().equals(accName)){
                            borrowers.set(i, new Tag(borrowers.get(i).getTagId()-1,accName));
                        }
                    }
                    this.BorrHolder.put(accName,temp);
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
    
}
