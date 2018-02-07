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
    private ControladorPagos cp;

    public ControladorMenu(ControladorClientes cc, Menu m, ControladorTarifas ct,ControladorPagos cp) {
        this.cc = cc;
        this.cp = cp;
        this.m = m;
        this.m.btnClientes.addActionListener(this);
        this.m.btnTarifas.addActionListener(this);
        this.m.acTabla.addActionListener(this);
        this.ct = ct;
        this.m.btnPagos.addActionListener(this);
    }

    public void iniciar() {

        m.setTitle("Menu");
        m.setLocationRelativeTo(null);
        m.setVisible(true);
        verificarVto();
        cargarTablaVto(modelo);
     

    }
    
    public void limpiarTabla() {

        int sizeModel = m.tablaVto.getRowCount();

        for (int i = 0; i < sizeModel; i++) {
            m.tablaVto.removeRowSelectionInterval(0, sizeModel - 1);
        }

    }

    public void actualizarTabla() {

        DefaultTableModel a = new DefaultTableModel();
        limpiarTabla();
        verificarVto();
        cargarTablaVto(a);

    }

public void verificarVto() {
        
        Calendar fechaHoy = Calendar.getInstance();
        

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
                
                Calendar fechaVto2 = Calendar.getInstance();
                Calendar fechaVto = Calendar.getInstance();
                
                fechaVto.setTime(rs.getDate(1));
                fechaVto2.setTime(rs.getDate(1));
                
                fechaVto.add(Calendar.DATE, -5);
                
                if(fechaHoy.after(fechaVto) && fechaHoy.before(fechaVto2)){
  
                    String sql2 = "UPDATE cuentas SET proxVto= true WHERE id=" + rs.getInt(2);
                    String sql3 = "UPDATE cuentas SET diasRestantes = 'CERCANO' WHERE id=" + rs.getInt(2);
                    
                    ps = con.prepareStatement(sql2);
                    ps.execute();
                    ps = con.prepareStatement(sql3);
                    ps.execute();
                 }   
                    
                 if(fechaHoy.equals(fechaVto2) || fechaHoy.after(fechaVto2)){
                        
                    String sql4 = "UPDATE cuentas SET proxVto= true WHERE id=" + rs.getInt(2);
                    String sql5 = "UPDATE cuentas SET diasRestantes = 'VENCIDO' WHERE id=" + rs.getInt(2);
                    
                    ps = con.prepareStatement(sql4);
                    ps.execute();
                    ps = con.prepareStatement(sql5);
                    ps.execute();
                  
                    
                }
                
            }

        } catch (SQLException ex) {
            Logger.getLogger(ControladorMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
        

    }

    public void cargarTablaVto(DefaultTableModel table) {

        try {
            PreparedStatement ps = null;
            m.tablaVto.setModel(table);

            ResultSet rs = null;
            Conexion conn = new Conexion();
            Connection con = conn.getConect();

            String sql = "SELECT a.dni, a.nombre, b.fechaVto, b.diasRestantes FROM cliente a, cuentas b WHERE a.dni = b.dniCliente and b.proxVto = true ";

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

            table.addColumn("DNI");
            table.addColumn("NOMBRE");
            table.addColumn("FECHA VENCIMIENTO");
            table.addColumn("ESTADO");

            try {
                while (rs.next()) {


                    Object[] filas = new Object[cantCol];
                    for (int i = 0; i < cantCol; i++) {
                    filas[i] = rs.getObject(i + 1);
                       
                    }
                    
                    table.addRow(filas);
                    
                }
                
            } catch (SQLException ex) {
                Logger.getLogger(ControladorMenu.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ControladorMenu.class.getName()).log(Level.SEVERE, null, ex);
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
        if(e.getSource() == m.btnPagos){
            
            cp.iniciar();
        }
        if (e.getSource() == m.acTabla) {

            actualizarTabla();

        }
    }

    public void iniciarVistaMenuCliente() throws SQLException {
        cc.iniciarMenuCliente();

    }
}
