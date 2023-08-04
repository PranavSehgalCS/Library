package com.library.persistance.Template;
///////////////////////////////////////////////////////////////////////////////////////////////////////
//  FILE : TemplateFileDAO.java
//  AUTHOR : Pranav Sehgal <PranavSehgalCS>
//  DESCRIPTION: IMPLEMENTS TemplateDAO interface and declares defined functions
///////////////////////////////////////////////////////////////////////////////////////////////////////
import java.util.Map;
import java.util.HashMap;

import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.DriverManager;

import com.library.model.Template;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
///////////////////////////////////////////////////////////////////////////////////////////////////////

@Component
public class TemplateFileDAO implements TemplateDAO{
    private Statement stat;
    private Connection conn;
    private int nextID;
    private Boolean updated = false; /* DECLARE this as public static if the database table is shared  
                                        and set this as false if that table is updated elsewhere*/
    private Map<Integer, Template> TemplateHolder = new HashMap<Integer, Template>();

    public TemplateFileDAO( @Value("${spring.datasource.url}") String database,
                            @Value("${spring.datasource.username}") String datauser,
                            @Value("${spring.datasource.password}") String datapass
                            ){
        try {
            this.conn = DriverManager.getConnection(database,datauser,datapass);
            this.stat = conn.createStatement();
            this.updated = this.loadTemplates();
        } catch (Exception e) {
            System.out.println("\nERROR While Initializing Connection --> " + e);
        }
    }
    
    private String qot(String plain){
        return (", '" + plain + "'");
    } 
    private Boolean loadTemplates(){
        try {
            this.TemplateHolder.clear();
            String com = "SELECT * FROM templates ORDER BY temid;" ;
            Template mapObj = new Template(0, " ", " ",false);
            ResultSet load = this.stat.executeQuery(com);

            while(load.next()){
                mapObj=new Template(load.getInt(    "temid"),
                                    load.getString( "tname"),
                                    load.getString( "tmess"),
                                    load.getBoolean("tbool")
                                    );
                TemplateHolder.put(mapObj.getTemid(), mapObj);
            }
            nextID = (mapObj.getTemid()+1);
            return true;
        } catch (Exception e) {
            System.out.println("ERROR While loading templates from database --> \n" + e);
        }
        return false;
    }
    private Boolean saveTemplates(String command){
        try {
            int i = this.stat.executeUpdate(command);
            if(i<1){return false;}
            this.updated = false;
            return true;
        } catch (Exception e) {
            System.out.println("Command used : "+command);
            System.out.println("ERROR While executing command : --> "+ e);
        }
        return false;
    }
    
    @Override
    public Template[] getTemplates(int temid){
        try {
            int index=-1;
            if(!this.updated){
                this.updated = loadTemplates();
            }
            Template[] retVal =  new Template[1];
            if(temid != -1){
                for(Template i: this.TemplateHolder.values()){
                    if(i.getTemid()==temid){
                        retVal[0]=i;
                        return retVal;
                    }
                }
                return retVal;
            } else{
                retVal =  new Template[this.TemplateHolder.size()];
                for(Template i: this.TemplateHolder.values()){
                    index++;
                    retVal[index]=i;
                }
                return retVal;
            }
        } catch (Exception e) {
            System.out.println("Error At Function While Getting With Id : " + temid);
            return null;
        }
    }
    
    @Override
    public Boolean createTemplate(String tname, String tmess, Boolean tbool){
        try {
            this.nextID ++;
            int temid = (this.nextID - 1);
            String command = "INSERT INTO templates VALUES(" + temid + qot(tname) + qot(tmess) + qot(tbool.toString()) + ");";
            Boolean retVal = saveTemplates(command);
            if(retVal){
                this.updated = false;
            }
            return retVal;
        }catch (Exception e) {
            System.out.println("ERROR at function while creating template --> " + e);
        }
        return false;
    }

    @Override
    public Boolean updateTemplate(int temid, String tname, String tmess, Boolean tbool){
        if(!this.updated){this.updated = loadTemplates();}
        try{
            Template currTemplate = null;
            Boolean retVal = true;
            for(Template i:TemplateHolder.values()){
                if(i.getTemid() == temid){
                    currTemplate = i;
                    break;
                }
            }
            if(currTemplate==null){
                return false;
            }
            if(!(tbool == currTemplate.getTbool())){
                retVal = retVal && saveTemplates("UPDATE templates SET tbool = '" + tbool + "' WHERE temid = " + temid + ";");
            }
            if(retVal && !(tname.equals(currTemplate.getTname()))){
                retVal = retVal && saveTemplates("UPDATE templates SET tname = '" + tname + "' WHERE temid = " + temid + ";");
            }
            if(retVal && !(tmess.equals(currTemplate.getTmess()))){
                retVal = retVal && saveTemplates("UPDATE templates SET tmess = '" + tmess + "' WHERE temid = " + temid + ";");
            }
            if(retVal){
                this.updated = false;
            }
            return retVal;
        }catch (Exception e) {
            System.out.println("ERROR at function while updating template --> " + e);
        }
        return false;
    }

    @Override
    public Boolean deleteTemplate(int temid){
        try {
            this.updated = false;
            TemplateHolder.remove(temid);
            return saveTemplates("DELETE FROM templates WHERE temid = " + temid + ";");
        } catch (Exception e) {
            System.out.println("ERROR at function while deleting template --> " + e);
        }
        return false;
    }
    
}
