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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
    private int band = -1;
    private boolean band2= false;

    public ControladorTarifas(Tarifas tf, EntidadTarifas et, Tarifa ta) {
        this.tf = tf;
        this.et = et;
        this.ta = ta;
        this.tf.btnGuardarTar.addActionListener(this);
        this.tf.btnNuevoTar.addActionListener(this);
        this.tf.btnElemTar.addActionListener(this);
        this.tf.btnModTar.addActionListener(this);
        //this.tf.btnConfirmar.addActionListener(this);
        this.tf.tablaTarifas.addMouseListener(new MouseAdapter(){

            @Override
            public void mouseClicked(MouseEvent e){

                int fila = tf.tablaTarifas.rowAtPoint(e.getPoint());

                int columna = tf.tablaTarifas.columnAtPoint(e.getPoint());

                if ((fila > -1) && (columna > -1)){

                    tf.btnModTar.setEnabled(true);
                    tf.btnElemTar.setEnabled(true);
                
                }
            }
        });
    }

    public void cargarTabla(){
        
      
        try {
            PreparedStatement ps = null;

            tf.tablaTarifas.setModel(modelo);

            ResultSet rs = null;
            Conexion conn = new Conexion();
            Connection con;
        
            con = conn.getConect();
        
           
        
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
            Logger.getLogger(ControladorTarifas.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ControladorTarifas.class.getName()).log(Level.SEVERE, null, ex);
        }
            

      }

    

    public void iniciarTarifas() {

        tf.setTitle("Tarifas");
        tf.setLocationRelativeTo(null);
        tf.setVisible(true);
        
        if (band2 == false) {
            cargarTabla();
            band2 = true;
        }

        tf.txtNombreTar.setEditable(false);
        tf.txtPrecio.setEditable(false);
        tf.txtId.setEditable(false);
        tf.btnGuardarTar.setEnabled(false);
        tf.btnModTar.setEnabled(false);
        tf.btnElemTar.setEnabled(false);
        
        

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == tf.btnGuardarTar) {

            ta.setNombre(tf.txtNombreTar.getText());
            ta.setPrecio(Double.parseDouble(tf.txtPrecio.getText()));
            ta.setId(Integer.parseInt(tf.txtId.getText()));

            System.out.println(ta);

            if (band == 0) {

                try {

                    if (et.registrar(ta)) {

                        JOptionPane.showMessageDialog(null, "Nueva tarifa creada");
                        modelo.addRow(new Object[]{ta.getId(), ta.getNombre(), ta.getPrecio()});

                        band = -1;

                    } else {
                        JOptionPane.showMessageDialog(null, "ERROR: Verifique los datos ingresados");
                    }

                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ControladorClientes.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if (band == 1) {
                try {
                    if (et.modificar(ta)) {
                        JOptionPane.showMessageDialog(null, "Cambios registrados exitosamente ");

                        modelo.setValueAt(ta.getId(), filaEditar, 0);
                        modelo.setValueAt(ta.getNombre(), filaEditar, 1);
                        modelo.setValueAt(ta.getPrecio(), filaEditar, 2);  
                        
                        tf.btnModTar.setEnabled(false);
                        tf.btnElemTar.setEnabled(false);

                        band = -1;

                    } else {
                        JOptionPane.showMessageDialog(null, "ERROR: Verifique los datos ingresados");
                    }
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ControladorClientes.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            tf.txtNombreTar.setText(null);
            tf.txtNombreTar.setEditable(false);
            tf.txtPrecio.setText(null);
            tf.txtPrecio.setEditable(false);
            tf.txtId.setText(null);
            tf.txtId.setEditable(false);
            tf.btnGuardarTar.setEnabled(false);

        }

        if (e.getSource() == tf.btnNuevoTar) {

            tf.txtNombreTar.setEditable(true);
            tf.txtPrecio.setEditable(true);
            tf.txtId.setEditable(true);
            tf.btnGuardarTar.setEnabled(true);
            band = 0;
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
                    JOptionPane.showMessageDialog(null, "Tarifa eliminada correctamente");
                    
                    tf.btnElemTar.setEnabled(false);
                    tf.btnModTar.setEnabled(false);
                    

                }

            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ControladorClientes.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (e.getSource() == tf.btnModTar) {

            filaEditar = tf.tablaTarifas.getSelectedRow();
            numFila = tf.tablaTarifas.getSelectedRowCount();
            tf.txtNombreTar.setEditable(true);
            tf.txtPrecio.setEditable(true);
            tf.btnGuardarTar.setEnabled(true);
            band = 1;
            
            if (filaEditar >= 0 && numFila == 1) {

                tf.txtId.setText(String.valueOf(tf.tablaTarifas.getValueAt(filaEditar, 0)));
                tf.txtId.setEditable(false);
                tf.txtNombreTar.setText((String) tf.tablaTarifas.getValueAt(filaEditar, 1));
                tf.txtPrecio.setText(String.valueOf(tf.tablaTarifas.getValueAt(filaEditar, 2)));
                

            }

        }
      
    }

}
