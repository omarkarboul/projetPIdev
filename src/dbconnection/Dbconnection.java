/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbconnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author karbo
 */
public class Dbconnection {
    
    private static Dbconnection instance;
    private Connection cnx;

    private final String URL = "jdbc:mysql://localhost:3306/pidev";
    private final String LOGIN = "root";
    private final String PASSWORD = "";

    private Dbconnection() {
        try {
            cnx = DriverManager.getConnection(URL, LOGIN, PASSWORD);
            System.out.println("Conncting !");
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    public static Dbconnection getInstance() {
        if (instance == null) {
            instance = new Dbconnection();
        }
        return instance;
    }

    public Connection getCnx() {
        return cnx;
    }
    
}
