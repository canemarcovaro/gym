/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.Cliente;
import Vista.Clientes;
import Vista.Menu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author JorgeA
 */
public class ControladorMenu implements ActionListener {

    private ControladorClientes cc;
    private Menu m;
    private ControladorTarifas ct;
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

    }
    
    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == m.btnClientes){
            try {
                iniciarVistaMenuCliente();
            } catch (SQLException ex) {
                Logger.getLogger(ControladorMenu.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if(e.getSource() == m.btnTarifas ){
            ct.iniciarTarifas();
        }
    }
    public void iniciarVistaMenuCliente() throws SQLException{
        cc.iniciarMenuCliente();
        
    }
}
