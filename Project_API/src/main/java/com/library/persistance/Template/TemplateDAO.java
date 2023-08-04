package com.library.persistance.Template;
///////////////////////////////////////////////////////////////////////////////////////////////////////
//  FILE : TemplateDAO.java
//  AUTHOR : Pranav Sehgal <PranavSehgalCS>
//  DESCRIPTION: Is a DAO.java INTERFACE file, used to DECLARE functions
///////////////////////////////////////////////////////////////////////////////////////////////////////
import java.io.IOException;
import com.library.model.Template;
import org.springframework.stereotype.Repository;

@Repository
public interface TemplateDAO{
    Template[] getTemplates(int temid);
    Boolean createTemplate(String tname, String tmess, Boolean tbool)throws IOException;
    Boolean updateTemplate(int temid, String tname, String tmess, Boolean tbool)throws IOException;
    Boolean deleteTemplate(int temid) throws IOException;
}