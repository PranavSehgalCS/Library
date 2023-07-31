package com.library.persistance;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
public class Commonfuncs {
    private static String dataBase;
    private static String dataUser;
    private static String dataPass;

    public Commonfuncs(String dataBase, String dataUser, String dataPass){
        Commonfuncs.dataBase = dataBase;
        Commonfuncs.dataPass = dataPass;
        Commonfuncs.dataUser = dataUser;
    }
    public String qot(String In){
        String q = ""+'"';
        String r = "'";
        In.replaceAll(q, "");
        In.replaceAll(r, "");
        return ("'"+In+"'");
    }

    public ResultSet getQuery(String query) throws SQLException{
        try {
            Statement stat =  DriverManager.getConnection(dataBase,dataUser,dataPass).createStatement();
            ResultSet retVal = stat.executeQuery(query);
            if(retVal == null){
                stat.getConnection().close();
            }
            return retVal;
        } catch (Exception e) {
            System.out.println("\n\nTRIGGER COMMAND : "+query); 
            System.out.println("\nERROR at getQuery(Commonfuncs) --> " +  e);
            throw new RuntimeException();
        }
    }
    public Boolean setQuery(String query)throws RuntimeException{
        try{
            Connection conn = DriverManager.getConnection(dataBase,dataUser,dataPass); 
            Statement stat = conn.createStatement();
            stat.executeUpdate(query);
            stat.close();
            conn.close();
            return true;
        }catch(Exception e){
            System.out.println("\n\nTRIGGER COMMAND : "+query); 
            System.out.println("\nERROR at setQuery(Commonfuncs) --> " +  e);
            throw new RuntimeException();
        }
    }
}
