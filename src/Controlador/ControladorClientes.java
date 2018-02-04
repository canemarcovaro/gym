/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.Cliente;
import Modelo.Conexion;
import Modelo.Cuenta;
import Modelo.Entidad;
import Modelo.EntidadCuenta;
import Modelo.EntidadTarifas;
import Vista.Clientes;
import Vista.MenuClientes;
import com.sun.webkit.graphics.WCImage;
import static com.sun.webkit.graphics.WCImage.getImage;
import static com.sun.xml.internal.fastinfoset.alphabet.BuiltInRestrictedAlphabets.table;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author JorgeA
 */
public class ControladorClientes implements ActionListener, KeyListener, MouseListener {

    private String foto;
    private File file;
    private Clientes c;
    private Cliente cl;
    private Entidad en;
    private MenuClientes mc;
    DefaultTableModel modelo = new DefaultTableModel();
    TableRowSorter trs;
    private int filaEditar;
    private int numFila;
    boolean band = true;
    private Cuenta cu;
    private EntidadTarifas et;
    private EntidadCuenta ec;

    public ControladorClientes(Clientes c, Cliente cl, Entidad en, MenuClientes mc, Cuenta cu, EntidadTarifas et,EntidadCuenta ec) {
        this.c = c;
        this.cl = cl;
        this.cu = cu;
        this.en = en;
        this.mc = mc;
        this.et = et;
        this.ec = ec;
        this.c.btnGuardar.addActionListener(this);
        this.mc.btnNuevo.addActionListener(this);
        this.c.btnSubir.addActionListener(this);
        this.mc.txtBuscar.addKeyListener(this);
        //this.mc.jcomboBuscar.addKeyListener(this);
        this.mc.btnMod.addActionListener(this);
        this.c.btnEdit.addActionListener(this);
        this.mc.tabla1.addMouseListener(this);
        this.mc.btnElim.addActionListener(this);
        this.c.btnCancel.addActionListener(this);
        this.c.comboTar.addActionListener(this);

    }

    public void iniciarClientes() {

        c.setTitle("Clientes");
        c.setLocationRelativeTo(null);
        c.setVisible(true);
        setComboTar();
        
        

    }

    public void iniciarMenuCliente() throws SQLException {

        mc.setTitle("Menu Clientes");
        mc.setLocationRelativeTo(null);
        mc.setVisible(true);

        //Fixed, se incorporó bandera para solucionar problema de Dispose con la Vista MenuClientes...
        if (band == true) {
            cargarTabla();
            band = false;
        }

    }
    public void calcularFecha(){
        
        Calendar fechaVal = Calendar.getInstance();
      
        Calendar fechaVto = Calendar.getInstance();
        fechaVto.add(Calendar.DATE, 30);
        
     
        cu.setFechaVal(new java.sql.Date(fechaVal.getTimeInMillis()));
        cu.setFechaVto(new java.sql.Date(fechaVto.getTimeInMillis()));
        

        System.out.println(cu.getFechaVal());
        
    
        
    }
    public void setComboTar() {

        PreparedStatement ps = null;

        ResultSet rs = null;
        Conexion conn = new Conexion();
        Connection con = null;
        try {
            con = conn.getConect();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ControladorClientes.class.getName()).log(Level.SEVERE, null, ex);
        }
        String sql = "SELECT id FROM tarifas";
        try {
            ps = con.prepareStatement(sql);
        } catch (SQLException ex) {
            Logger.getLogger(EntidadTarifas.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            rs = ps.executeQuery();
            c.comboTar.removeAllItems();
            while(rs.next()){
                c.comboTar.addItem(rs.getString(1) );
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(EntidadTarifas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void cargarTabla() throws SQLException {
        try {
            PreparedStatement ps = null;

            mc.tabla1.setModel(modelo);
            ResultSet rs = null;
            Conexion conn = new Conexion();
            Connection con = conn.getConect();
            String sql = "SELECT dni,nombre,fechaNac,nroTel,direccion,mail FROM cliente";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            ResultSetMetaData rsmd = rs.getMetaData();
            int cantCol = rsmd.getColumnCount();
            modelo.addColumn("DNI");
            modelo.addColumn("NOMBRE");
            modelo.addColumn("FECHA NACIMIENTO");
            modelo.addColumn("TELEFONO");
            modelo.addColumn("DOMICILIO");
            modelo.addColumn("MAIL");
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

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == mc.btnNuevo) {
            c.btnGuardar.setVisible(true);

            iniciarClientes();
            //mc.dispose();
            c.btnEdit.setVisible(false);
        }
        if (e.getSource() == c.btnGuardar) {

            SimpleDateFormat formatter = new SimpleDateFormat("aa/MM/dddd");

            cl.setDni(Integer.parseInt(c.txtDni.getText()));
            cl.setNombre(c.txtNombre.getText());

            cl.setFechaNac(new java.sql.Date(c.fecha.getDate().getTime()));

            cl.setNroTel(Integer.parseInt(c.txtNum.getText()));
            cl.setDirec(c.txtDirec.getText());
            cl.setMail(c.txtEmail.getText());
            cl.setRuta(foto);
            //Atributos de cuenta:
            cu.setDniCliente(Integer.parseInt(c.txtDni.getText()));
            //cu.setIdTarifa(Integer.parseInt((String)c.comboTar.getSelectedItem()));
            cu.setIdTarifa(55);
            System.out.println(c.comboTar.getSelectedItem());
            
            calcularFecha();
            
            
            
            
            

            try {
                if (en.registrar(cl) && ec.registrar(cu)) {
                    JOptionPane.showMessageDialog(null, "registro guardado");
                    modelo.addRow(new Object[]{cl.getDni(), cl.getNombre(), cl.getFechaNac(), cl.getNroTel(), cl.getDirec(), cl.getMail()});
                } else {
                    JOptionPane.showMessageDialog(null, "error al guardar");
                    en.eliminar(cl);
                }
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ControladorClientes.class.getName()).log(Level.SEVERE, null, ex);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ControladorClientes.class.getName()).log(Level.SEVERE, null, ex);
            }

            
            c.dispose();

        }
        if (e.getSource() == c.btnSubir) {
            FileNameExtensionFilter filtro = new FileNameExtensionFilter("Formato de Archivos: JPEG(*.JPG;*.JPEG)", "jpg", "jpeg");
            JFileChooser archivo = new JFileChooser();
            archivo.addChoosableFileFilter(filtro);
            archivo.setDialogTitle("Abrir imagen");
            int ventana = archivo.showOpenDialog(null);
            if (ventana == JFileChooser.APPROVE_OPTION) {

                file = archivo.getSelectedFile();
                foto = String.valueOf(file);
                Toolkit toolkit = Toolkit.getDefaultToolkit();
                Image image1 = toolkit.getImage(foto);
                image1 = image1.getScaledInstance(202, 241, Image.SCALE_DEFAULT);
                c.labFoto.setIcon(new ImageIcon(image1));

            }
        }
        if (e.getSource() == mc.btnMod) {
            c.btnEdit.setVisible(true);
            filaEditar = mc.tabla1.getSelectedRow();
            numFila = mc.tabla1.getSelectedRowCount();
            if (filaEditar >= 0 && numFila == 1) {
                iniciarClientes();
                c.txtDni.setText(String.valueOf(mc.tabla1.getValueAt(filaEditar, 0)));
                c.txtDni.setEditable(false);
                c.txtNombre.setText(String.valueOf(mc.tabla1.getValueAt(filaEditar, 1)));
                c.fecha.setDate((Date) mc.tabla1.getValueAt(filaEditar, 2));
                c.txtNum.setText(String.valueOf(mc.tabla1.getValueAt(filaEditar, 3)));
                c.txtDirec.setText(String.valueOf(mc.tabla1.getValueAt(filaEditar, 4)));
                c.txtEmail.setText(String.valueOf(mc.tabla1.getValueAt(filaEditar, 5)));

                try {
                    ImageIcon image1 = en.traerFoto((int) mc.tabla1.getValueAt(filaEditar, 0));
                    Icon icono = new ImageIcon(image1.getImage().getScaledInstance(202, 241, Image.SCALE_DEFAULT));

                    c.labFoto.setIcon(icono);

                } catch (SQLException ex) {
                    Logger.getLogger(ControladorClientes.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ControladorClientes.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(ControladorClientes.class.getName()).log(Level.SEVERE, null, ex);
                }
                c.btnGuardar.setVisible(false);

            }

        }
        if (e.getSource() == c.btnEdit) {

            SimpleDateFormat formatter = new SimpleDateFormat("aa/MM/dddd");

            cl.setDni(Integer.parseInt(c.txtDni.getText()));
            cl.setNombre(c.txtNombre.getText());
            cl.setFechaNac(new java.sql.Date(c.fecha.getDate().getTime()));
            cl.setNroTel(Integer.parseInt(c.txtNum.getText()));
            cl.setDirec(c.txtDirec.getText());
            cl.setMail(c.txtEmail.getText());
            cl.setRuta(foto);
            c.dispose();
            try {
                if (en.modificar(cl)) {
                    JOptionPane.showMessageDialog(null, "registro modificado");

                    modelo.setValueAt(cl.getDni(), filaEditar, 0);
                    modelo.setValueAt(cl.getNombre(), filaEditar, 1);
                    modelo.setValueAt(cl.getFechaNac(), filaEditar, 2);

                    modelo.setValueAt(cl.getNroTel(), filaEditar, 3);
                    modelo.setValueAt(cl.getDirec(), filaEditar, 4);
                    modelo.setValueAt(cl.getMail(), filaEditar, 5);

                } else {
                    JOptionPane.showMessageDialog(null, "error al modificar");
                }
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ControladorClientes.class.getName()).log(Level.SEVERE, null, ex);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ControladorClientes.class.getName()).log(Level.SEVERE, null, ex);
            }
            //c.dispose();

        }
        if (e.getSource() == mc.btnElim) {
            Object[] botones = {" Confirmar", " Cancelar"};
            int variable = JOptionPane.showOptionDialog(null, " Estas seguro que quieres eliminar este registro?", "Eliminar Registro", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null/*icono*/, botones, botones[0]);

            int filaEditar = mc.tabla1.getSelectedRow();
            int id = (int) mc.tabla1.getValueAt(filaEditar, 0);

            cl.setDni(id);
            try {
                if (JOptionPane.OK_OPTION == variable) {

                    en.eliminar(cl);

                    modelo.removeRow(filaEditar);

                }

            } catch (FileNotFoundException ex) {
                Logger.getLogger(ControladorClientes.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ControladorClientes.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (e.getSource() == c.btnCancel) {
            c.dispose();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getSource() == mc.txtBuscar) {
            trs = new TableRowSorter(modelo);
            mc.tabla1.setRowSorter(trs);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getSource() == mc.txtBuscar) {

            trs.setRowFilter(RowFilter.regexFilter(mc.txtBuscar.getText(), 0));

        }

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 1) {
            int filaEditar = mc.tabla1.getSelectedRow();
            int numFila = mc.tabla1.getSelectedRowCount();
            ImageIcon image1 = null;
            try {
                image1 = en.traerFoto((int) mc.tabla1.getValueAt(filaEditar, 0));
            } catch (SQLException ex) {
                Logger.getLogger(ControladorClientes.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ControladorClientes.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ControladorClientes.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (image1 != null) {
                Icon icono = new ImageIcon(image1.getImage().getScaledInstance(250, 300, Image.SCALE_DEFAULT));

                mc.labFoto2.setIcon(icono);

            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

}
