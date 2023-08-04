package com.library.model;
///////////////////////////////////////////////////////////////////////////////////////////////////////    
//  FILE : Borrow.java
//  AUTHOR : Pranav Sehgal <PranavSehgalCS>
//  DESCRIPTION: Is a public model to encapsulate borrow data
///////////////////////////////////////////////////////////////////////////////////////////////////////
public class Borrow {
    private int bookId;
    private String borrDate;
    private String accName;
    private String bookName;

    public Borrow(String accName, String borrDate,int bookId, String bookName){
        this.bookId = bookId;
        this.accName = accName;
        this.borrDate = borrDate;
        this.bookName = bookName;
    }

    public int getBookId(){
        return bookId;
    }
    public String getBorrDate(){
        return borrDate;
    }
    public String getAccName(){
        return accName;
    }
    public String getBookName(){
        return bookName;
    }
}