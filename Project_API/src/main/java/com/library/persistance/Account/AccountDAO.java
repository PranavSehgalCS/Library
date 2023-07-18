package com.library.persistance.Account;
///////////////////////////////////////////////////////////////////////////////////////////////////////
//  FILE : AccountDAO.java
//  AUTHOR : Pranav Sehgal <PranavSehgalCS>
//  DESCRIPTION: Is a DAO.java INTERFACE file, used to DECLARE account functions
//
///////////////////////////////////////////////////////////////////////////////////////////////////////

import org.springframework.stereotype.Repository;

import com.library.model.Account;

@Repository
public interface AccountDAO{

    Account getAccount(String accName, String accPass);
    int createAccount(String accName, String accPass);
    boolean updateAccount(int accID, String accName, String accPass);
    boolean deleteAccount(int accID,String accPass);
}
