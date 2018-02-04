/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import Controlador.ControladorClientes;
import Controlador.ControladorMenu;
import Controlador.ControladorTarifas;
import Modelo.Cliente;
import Modelo.Conexion;
import Modelo.Cuenta;
import Modelo.Entidad;
import Modelo.EntidadCuenta;
import Modelo.EntidadTarifas;
import Modelo.Tarifa;
import Vista.Clientes;
import Vista.Menu;
import Vista.MenuClientes;
import Vista.Tarifas;


/**
 *
 * @author JorgeA
 */
public class Main {
    
    public static void main(String[] args){
        
        Clientes cl = new Clientes();
        Menu m = new Menu();
        Cliente c1 = new Cliente();
        Conexion cn = new Conexion();
        Entidad en = new Entidad();
        MenuClientes mc = new MenuClientes();
        Tarifas tf = new Tarifas();
        Tarifa ta = new Tarifa();
        EntidadTarifas et = new EntidadTarifas();
        ControladorTarifas ct = new ControladorTarifas(tf,et,ta);
        Cuenta cu = new Cuenta();
        EntidadCuenta ec = new EntidadCuenta();
        ControladorClientes col = new ControladorClientes(cl,c1,en,mc,cu,et,ec);
        ControladorMenu com = new ControladorMenu(col,m,ct);
        
        com.iniciar();
      
        
        
    }
}
