/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tservice.BD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author LuisAndres
 */
public class Connect {
    
    
    public static List<String> runQuery(String sQuery) throws ClassNotFoundException, SQLException{
       
        ArrayList<String> resultado = new ArrayList<String> ();
      
        Connection conn = DriverManager.getConnection( "jdbc:mysql://desarrollo.is.escuelaing.edu.co:3306/coswg2",
        "coswg2", "coswg20215" );
        try {
            Statement stmt = conn.createStatement();
            try {
            
                ResultSet rs = stmt.executeQuery(sQuery);
                
                try {
                    
                    while ( rs.next() ) {
                        
                        int numColumns = rs.getMetaData().getColumnCount();
                        for ( int i = 1 ; i <= numColumns ; i++ ) {
                            //System.out.println( "COLUMN " + i + " = " + rs.getObject(i) );
                            resultado.add(rs.getObject(i).toString());
                        }
                    }
                } finally {
                    try { rs.close(); } catch (Throwable ignore) { /* Propagate the original exception
                    instead of this one that you may want just logged */ }
                }
            } finally {
                try { stmt.close(); } catch (Throwable ignore) { /* Propagate the original exception
                instead of this one that you may want just logged */ }
            }
                } finally {
                    //It's important to close the connection when you are done with it
                    try { conn.close(); } catch (Throwable ignore) { /* Propagate the original exception
                    instead of this one that you may want just logged */ }
            }
    return resultado;
 }
}
