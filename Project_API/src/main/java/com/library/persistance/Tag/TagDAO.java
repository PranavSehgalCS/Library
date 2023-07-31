package com.library.persistance.Tag;
///////////////////////////////////////////////////////////////////////////////////////////////////////
//  FILE : TadDAO.java
//  AUTHOR : Pranav Sehgal <PranavSehgalCS>
//  DESCRIPTION: Is a DAO.java INTERFACE file, used to DECLARE functions
///////////////////////////////////////////////////////////////////////////////////////////////////////
import com.library.model.Tag;
import org.springframework.stereotype.Repository;

@Repository
public interface TagDAO {
    public Tag[] getAllTags(int i);
    public boolean deleteTag(int tagId);
    public boolean createTag(String tagName);
    public boolean updateTag(int tagId, String TagName);
}
