package com.tmgreyhat.esbobi.controllers;

import java.sql.*;


public class DBCON {




    public Connection getConnection() {

        Connection connection ;

        connection = null;
        try {
            Class.forName("com.ibm.db2.jcc.DB2Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        String url = "jdbc:db2://10.136.192.171:50000/ESBDB";
        // String url = "jdbc:as400://10.136.192.170/ESBDB;";
        try {
            connection = DriverManager.getConnection(url,"db2inst1", "notes");
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return  connection;
    }
}
