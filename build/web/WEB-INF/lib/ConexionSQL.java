/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.Registro;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jmazzetti
 */
public class ConexionSQL {

    public static Connection getConexion() {

        String jdbcDriver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        try {
            Class.forName(jdbcDriver).newInstance();
            System.out.println("JDBC driver loaded");
        } catch (Exception err) {
            System.err.println("Error loading JDBC driver");
            err.printStackTrace(System.err);
            System.exit(0);
        }
        String URLconexion = "jdbc:sqlserver://192.168.0.172:1433;databaseName=ValuacionFiscalAutomotores;database=ValuacionFiscalAutomotores;user=sa;password=123456.a";
        Connection conexion = null;
        try {
            conexion = DriverManager.getConnection(URLconexion);
        } catch (SQLException ex) {
            Logger.getLogger(ConexionSQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return conexion;
    }
}
