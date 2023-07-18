package com.library.model;
///////////////////////////////////////////////////////////////////////////////////////////////////////    
//  FILE : Account.java
//  AUTHOR : Pranav Sehgal <PranavSehgalCS>
//
//  DESCRIPTION: Is an account model used to encapsulate data
///////////////////////////////////////////////////////////////////////////////////////////////////////

public class Account {
    private int accID;
    private String accName;
    private String accPass;
    private boolean isAdmin;
    public Account(int accID, String accName, String accPass, boolean isAdmin){
        this.accID = accID;
        this.accName = accName;
        this.accPass = accPass;
        this.isAdmin = isAdmin;
    }
    public int getAccId(){
        return this.accID;
    }
    public String getAccName(){    
        return this.accName;
    }
    public String getAccPass(){
        return this.accPass;
    }
    public boolean getIsAdmin(){
        return this.isAdmin;
    }
}
