package com.library.model;
///////////////////////////////////////////////////////////////////////////////////////////////////////    
//  FILE : Book.java
//  AUTHOR : Pranav Sehgal <PranavSehgalCS>
//
//  DESCRIPTION: Is a public book Model used to encapsulate data
///////////////////////////////////////////////////////////////////////////////////////////////////////

public class Book {
    private int bookId;
    private int[] ageRange = new int[2];
    private String bookName;
    private String bookAuth;
    public String[] bookTags;
    
    public Book(int bookId, String bookName, String bookAuth, int[] ageRange, String[] bookTags){
        this.bookId = bookId;
        this.ageRange = ageRange;
        this.bookName = bookName;
        this.bookAuth = bookAuth;
        this.bookTags = bookTags;
    }
    
    
    public int getBookId(){
        return bookId;
    }
    public int[] getAgeRange(){
        return this.ageRange;
    }    
    public String getBookName(){
        return bookName;
    }
    public String getBookAuth(){
        return this.bookAuth;
    }
    public String[] getBookTags(){
        return bookTags;
    }
}
