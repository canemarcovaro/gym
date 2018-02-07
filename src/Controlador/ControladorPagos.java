/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.Conexion;
import Modelo.Pagos;
import Vista.VistaPagos;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author JorgeA
 */
public class ControladorPagos implements ActionListener {

    private Pagos p;
    private VistaPagos pp;
    DefaultTableModel modelo = new DefaultTableModel();
    private boolean band = false;

    public ControladorPagos(Pagos p, VistaPagos pp) {

        this.p = p;
        this.pp = pp;
        this.pp.btnCalcular.addActionListener(this);
    }

    public void iniciar() {

        pp.setTitle("Pagos");
        pp.setLocationRelativeTo(null);
        pp.setVisible(true);
        
        if(band == false){
        cargarTablaPagos();
        band = true;
        
    }
    }
    public void cargarTablaPagos() {

        try {

            PreparedStatement ps = null;
            pp.tablaPagos.setModel(modelo);

            ResultSet rs = null;
            Conexion conn = new Conexion();
            Connection con = conn.getConect();

            String sql = "SELECT a.id, a.importe, a.fechaPago, a.estado, b.nombre, b.dni FROM pagos a, cliente b, cuentas c WHERE a.idCuenta = c.id and b.dni = c.dniCliente";

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

            modelo.addColumn("ID");
            modelo.addColumn("NOMBRE");
            modelo.addColumn("IMPORTE");
            modelo.addColumn("FECHA PAGO");
            modelo.addColumn("ESTADO");
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
            Logger.getLogger(ControladorMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == pp.btnCalcular) {
            try {

                PreparedStatement ps = null;

                ResultSet rs = null;
                Conexion conn = new Conexion();
                Connection con = conn.getConect();

                Date fechaInicio = new java.sql.Date(pp.dChoInicio.getDate().getTime());
                Date fechaFin = new java.sql.Date(pp.dChoFin.getDate().getTime());

                String fecha1 = fechaInicio.toString();
                String fecha2 = fechaFin.toString();
                
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");  
                String fechaConFormatoFin = sdf.format(fechaFin);
                String fechaConFormatoInicio = sdf.format(fechaInicio);
                
                System.out.println(fechaConFormatoFin);
                
                String sql = "SELECT SUM(importe)FROM pagos WHERE fechaPago BETWEEN'" + fechaConFormatoFin + "'AND'" + fechaConFormatoInicio + "'";
                
                System.out.println(sql);
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

                try {
                    while (rs.next()) {
                       String resultado = rs.getString(1);
                        pp.txtRes.setText("$ "+resultado);
                        System.out.println(resultado);
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(ControladorPagos.class.getName()).log(Level.SEVERE, null, ex);
                }

            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ControladorMenu.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
