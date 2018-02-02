/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author JorgeA
 */
public class Conexion {

    Connection conect = null;

    public Connection getConect() throws ClassNotFoundException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conect = (Connection) DriverManager.getConnection("jdbc:mysql://localhost/clientesGym", "root", "yerba10");
            
            
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error de conexion ");
            JOptionPane.showMessageDialog(null, "error de conexion" + e);
        }
        return conect;
    }
}
