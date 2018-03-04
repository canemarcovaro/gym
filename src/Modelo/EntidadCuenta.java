/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author JorgeA
 */
public class EntidadCuenta extends Conexion {

    public boolean registrar(Cuenta c) {
        PreparedStatement ps = null;
        try {
            Connection con = getConect();
            String sql = "INSERT INTO cuentas (id,dniCliente,idTarifa,saldoAcredor,saldoDeudor,fechaVto,fechaVal,proxVto)  VALUES (NULL,?,?,?,?,?,?,false)";
            try {
                ps = con.prepareStatement(sql);
                
                ps.setInt(1, c.getDniCliente());
                ps.setInt(2, c.getIdTarifa());
                ps.setDouble(3, 40);
                ps.setDouble(4, 55);
                ps.setDate(5, c.getFechaVto());
                ps.setDate(6, c.getFechaVal());
               

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
}
