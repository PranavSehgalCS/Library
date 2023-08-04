package com.library.persistance.Borrow;
///////////////////////////////////////////////////////////////////////////////////////////////////////
//  FILE : TemplateDAO.java
//  AUTHOR : Pranav Sehgal <PranavSehgalCS>
//  DESCRIPTION: Is a DAO.java INTERFACE file, used to DECLARE functions
///////////////////////////////////////////////////////////////////////////////////////////////////////
import com.library.model.Tag;
import com.library.model.Borrow;
import org.springframework.stereotype.Repository;

@Repository
public interface BorrowDAO {
    Tag[] getBorrowers();
    Borrow[] getMyBorrows(String accName);
    Boolean createBorrow(String accName, String borrDate,int bookId, String bookName);
    Boolean deleteBorrow(String accName, int bookId);
    
}
