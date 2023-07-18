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
        return ("'"+In+"'");
    }
    public ResultSet getResult(String query) throws SQLException{
        Statement stat =  DriverManager.getConnection(dataBase,dataUser,dataPass).createStatement();
        ResultSet retVal = stat.executeQuery(query);
        if(retVal == null){
            stat.getConnection().close();
        }
        return retVal;
    }
    public Boolean setQuery(String query){
        try{
            Connection conn = DriverManager.getConnection(dataBase,dataUser,dataPass); 
            Statement stat = conn.createStatement();
            stat.executeUpdate(query);
            stat.close();
            conn.close();
            return true;
        }catch(Exception e){
            System.out.println("\nERROR at setQuery(Commonfuncs) --> " +  e);
            return false;
        }
    }
}
