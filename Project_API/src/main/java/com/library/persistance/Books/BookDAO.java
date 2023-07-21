package com.library.persistance.Books;
///////////////////////////////////////////////////////////////////////////////////////////////////////
//  FILE : TemplateDAO.java
//  AUTHOR : Pranav Sehgal <PranavSehgalCS>
//  DESCRIPTION: Is A DAO.java INTERFACE file, used to DECLARE functions accessible to controller
///////////////////////////////////////////////////////////////////////////////////////////////////////
import com.library.model.Book;
import org.springframework.stereotype.Repository;

@Repository
public interface BookDAO {
    Book[] getBookById(int bookId) throws RuntimeException;
    Book[] getBooksByFilter(String Author, int[] ageRange, String[] bookTags) throws RuntimeException;
    boolean createBook(String bookName, String bookAuth, int[] ageRange,String[] bookTags) throws RuntimeException;
    boolean updateBook(int bookId, String bookName, String bookAuth, int[] ageRange, String[] bookTags) throws RuntimeException;
    boolean deleteBook(int bookId) throws RuntimeException;
}
