/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.Cliente;
import Modelo.Conexion;
import Vista.Clientes;
import Vista.Menu;
import Vista.MenuClientes;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author JorgeA
 */
public class ControladorMenu implements ActionListener {

    private ControladorClientes cc;
    private Menu m;
    private ControladorTarifas ct;
    DefaultTableModel modelo = new DefaultTableModel();

    public ControladorMenu(ControladorClientes cc, Menu m, ControladorTarifas ct) {
        this.cc = cc;
        this.m = m;
        this.m.btnClientes.addActionListener(this);
        this.m.btnTarifas.addActionListener(this);
        this.ct = ct;
    }

    public void iniciar() {

        m.setTitle("Menu");
        m.setLocationRelativeTo(null);
        m.setVisible(true);
        verificarVto();
        cargarTablaVto();

    }

    public void verificarVto() {
        Calendar fecha = Calendar.getInstance();
        fecha.add(Calendar.DATE, -5);
        Date fecha2 = new java.sql.Date(fecha.getTimeInMillis());

        try {

            PreparedStatement ps = null;

            ResultSet rs = null;
            Conexion conn = new Conexion();
            Connection con = null;
            try {
                con = conn.getConect();
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ControladorMenu.class.getName()).log(Level.SEVERE, null, ex);
            }
            String sql = "SELECT fechaVto,id FROM cuentas";
            try {
                ps = con.prepareStatement(sql);
            } catch (SQLException ex) {
                Logger.getLogger(ControladorMenu.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                rs = ps.executeQuery();
            } catch (SQLException ex) {
                Logger.getLogger(ControladorMenu.class.getName()).log(Level.SEVERE, null, ex);
            }

            while (rs.next()) {

                if (rs.getDate(1).compareTo(fecha2) >= 0) {
                    String sql2 = "UPDATE cuentas SET proxVto= true WHERE id=" + rs.getInt(2);
                    ps = con.prepareStatement(sql2);
                    ps.execute();
                }

            }

        } catch (SQLException ex) {
            Logger.getLogger(ControladorMenu.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void cargarTablaVto() {

        try {
            PreparedStatement ps = null;
            m.tablaVto.setModel(modelo);

            ResultSet rs = null;
            Conexion conn = new Conexion();
            Connection con = conn.getConect();
            String sql = "SELECT a.nombre, a.dni, b.fechaVto FROM cliente a, cuentas b WHERE a.dni = b.dniCliente and b.proxVto = true ";
            try {
                ps = con.prepareStatement(sql);
            } catch (SQLException ex) {
                Logger.getLogger(ControladorMenu.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                rs = ps.executeQuery();
            } catch (SQLException ex) {
                Logger.getLogger(ControladorMenu.class.getName()).log(Level.SEVERE, null, ex);
            }

            ResultSetMetaData rsmd = null;
            try {
                rsmd = rs.getMetaData();
            } catch (SQLException ex) {
                Logger.getLogger(ControladorMenu.class.getName()).log(Level.SEVERE, null, ex);
            }
            int cantCol = 0;
            try {
                cantCol = rsmd.getColumnCount();
            } catch (SQLException ex) {
                Logger.getLogger(ControladorMenu.class.getName()).log(Level.SEVERE, null, ex);
            }
            modelo.addColumn("DNI");
            modelo.addColumn("NOMBRE");
            modelo.addColumn("FECHA VENCIMIENTO");
            try {
                while (rs.next()) {
                    Object[] filas = new Object[cantCol];
                    for (int i = 0; i < cantCol; i++) {
                        filas[i] = rs.getObject(i + 1);

                    }
                    modelo.addRow(filas);

                }
            } catch (SQLException ex) {
                Logger.getLogger(ControladorMenu.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ControladorClientes.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == m.btnClientes) {
            try {
                iniciarVistaMenuCliente();
            } catch (SQLException ex) {
                Logger.getLogger(ControladorMenu.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (e.getSource() == m.btnTarifas) {
            ct.iniciarTarifas();
        }
    }

    public void iniciarVistaMenuCliente() throws SQLException {
        cc.iniciarMenuCliente();

    }
}
