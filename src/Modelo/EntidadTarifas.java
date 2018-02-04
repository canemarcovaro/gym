/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author JorgeA
 */
public class EntidadTarifas extends Conexion {

    public boolean registrar(Tarifa t) {
        PreparedStatement ps = null;
        try {
            Connection con = getConect();
            String sql = "INSERT INTO tarifas (id,nombre,precio)  VALUES (?,?,?)";
            try {
                ps = con.prepareStatement(sql);
                ps.setInt(1, t.getId());
                ps.setString(2, t.getNombre());
                ps.setDouble(3, t.getPrecio()); 
                
                ps.execute();
                con.close();
                return true;
            } catch (SQLException ex) {
                Logger.getLogger(EntidadTarifas.class.getName()).log(Level.SEVERE, null, ex);

            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(EntidadTarifas.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return false;
      

    }
    public boolean eliminar(Tarifa t) throws ClassNotFoundException {
        PreparedStatement ps = null;
       
        Connection con = getConect();
        String sql = "DELETE FROM tarifas WHERE id=?";

        try {
           
                ps = con.prepareStatement(sql);
                ps.setInt(1,t.getId() );

                ps.execute();

            

            
            return true;
        } catch (SQLException ex) {
            System.err.println(ex);
            return false;

        } finally {
            try {
                con.close();

            } catch (SQLException ex) {
                System.err.println(ex);
            }

        }
    }
     public boolean modificar(Tarifa t) throws ClassNotFoundException {
        PreparedStatement ps = null;
        
        Connection con = getConect();
        
        String sql = "UPDATE tarifas SET nombre=?, precio=? WHERE id=?";

        try {
            
                ps = con.prepareStatement(sql);
                ps.setString(1, t.getNombre());
                ps.setDouble(2, t.getPrecio());
                ps.setInt(3, t.getId());

                System.out.println(ps);
                ps.execute();

           
            return true;
        } catch (SQLException ex) {
            System.err.println(ex);
            return false;

        } finally {
            try {
                con.close();

            } catch (SQLException ex) {
                System.err.println(ex);
            }

        }
    }
    

}
