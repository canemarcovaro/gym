/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.Conexion;
import Modelo.EntidadTarifas;
import Modelo.Tarifa;
import Vista.Tarifas;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author JorgeA
 */
public class ControladorTarifas implements ActionListener {

    private Tarifas tf;
    private Tarifa ta;
    private EntidadTarifas et;
    DefaultTableModel modelo = new DefaultTableModel();
    private int numFila;
    private int filaEditar;
            

    public ControladorTarifas(Tarifas tf, EntidadTarifas et, Tarifa ta) {
        this.tf = tf;
        this.et = et;
        this.ta = ta;
        this.tf.btnGuardarTar.addActionListener(this);
        this.tf.btnNuevoTar.addActionListener(this);
        this.tf.btnElemTar.addActionListener(this);
        this.tf.btnModTar.addActionListener(this);
        this.tf.btnConfirmar.addActionListener(this);
    }
 public void cargarTabla() throws SQLException {
        try {
            PreparedStatement ps = null;
           
            tf.tablaTarifas.setModel(modelo);
           
            ResultSet rs = null;
            Conexion conn = new Conexion();
            Connection con = conn.getConect();
            String sql = "SELECT id,nombre,precio FROM tarifas";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            ResultSetMetaData rsmd = rs.getMetaData();
            int cantCol = rsmd.getColumnCount();
            modelo.addColumn("ID");
            modelo.addColumn("NOMBRE");
            modelo.addColumn("PRECIO");
      
            while (rs.next()) {
                Object[] filas = new Object[cantCol];
                for (int i = 0; i < cantCol; i++) {
                    filas[i] = rs.getObject(i + 1);

                }
                modelo.addRow(filas);

            }

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ControladorClientes.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    public void iniciarTarifas() {

        tf.setTitle("Tarifas");
        tf.setLocationRelativeTo(null);
        tf.setVisible(true);
        try {
            cargarTabla();
        } catch (SQLException ex) {
            Logger.getLogger(ControladorTarifas.class.getName()).log(Level.SEVERE, null, ex);
        }

        tf.txtNombreTar.setEditable(false);
        tf.txtPrecio.setEditable(false);
        tf.txtId.setEditable(false);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == tf.btnGuardarTar) {
            ta.setNombre(tf.txtNombreTar.getText());
            ta.setPrecio(Double.parseDouble(tf.txtPrecio.getText()));
            ta.setId(Integer.parseInt(tf.txtId.getText()));

            System.out.println(ta);
            et.registrar(ta);
            modelo.addRow(new Object[]{ta.getId(),ta.getNombre(),ta.getPrecio()});
            
        }
        if (e.getSource() == tf.btnNuevoTar) {

            tf.txtNombreTar.setEditable(true);
            tf.txtPrecio.setEditable(true);
            tf.txtId.setEditable(true);
        }
        if (e.getSource() == tf.btnElemTar) {
            Object[] botones = {" Confirmar", " Cancelar"};
            int variable = JOptionPane.showOptionDialog(null, " Estas seguro que quieres eliminar este registro?", "Eliminar Registro", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null/*icono*/, botones, botones[0]);

            int filaEditar = tf.tablaTarifas.getSelectedRow();
            int id = (int) tf.tablaTarifas.getValueAt(filaEditar, 0);

            ta.setId(id);
            try {
                if (JOptionPane.OK_OPTION == variable) {

                    et.eliminar(ta);

                    modelo.removeRow(filaEditar);

                }

            }  catch (ClassNotFoundException ex) {
                Logger.getLogger(ControladorClientes.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (e.getSource() == tf.btnModTar) {

             filaEditar = tf.tablaTarifas.getSelectedRow();
             numFila = tf.tablaTarifas.getSelectedRowCount();
            tf.txtNombreTar.setEditable(true);
            tf.txtPrecio.setEditable(true);
            if (filaEditar >= 0 && numFila == 1) {
               
                tf.txtId.setText(String.valueOf(tf.tablaTarifas.getValueAt(filaEditar, 0)));
                tf.txtId.setEditable(false);
                tf.txtNombreTar.setText((String) tf.tablaTarifas.getValueAt(filaEditar, 1));
                tf.txtPrecio.setText(String.valueOf(tf.tablaTarifas.getValueAt(filaEditar, 2)));
                tf.btnConfirmar.setVisible(true);
                
              

            }

        }
        if(e.getSource() == tf.btnConfirmar){
            ta.setNombre(tf.txtNombreTar.getText());
            ta.setPrecio(Double.parseDouble(tf.txtPrecio.getText()));
            ta.setId(Integer.parseInt(tf.txtId.getText()));
             try {
                if (et.modificar(ta)) {
                    JOptionPane.showMessageDialog(null, "registro modificado");
                    
                    modelo.setValueAt(ta.getId(), filaEditar, 0);
                    modelo.setValueAt(ta.getNombre(), filaEditar, 1);
                    modelo.setValueAt(ta.getPrecio(), filaEditar, 2);
                   
                   
                 
                } else {
                    JOptionPane.showMessageDialog(null, "error al modificar");
                }
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ControladorClientes.class.getName()).log(Level.SEVERE, null, ex);
            } 
        }
    }
    
    

}
