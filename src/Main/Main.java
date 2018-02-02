/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import Controlador.ControladorClientes;
import Controlador.ControladorMenu;
import Modelo.Cliente;
import Modelo.Conexion;
import Modelo.Entidad;
import Vista.Clientes;
import Vista.Menu;
import Vista.MenuClientes;


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
        
        ControladorClientes col = new ControladorClientes(cl,c1,en,mc);
        ControladorMenu com = new ControladorMenu(col,m);
        
        com.iniciar();
      
        
        
    }
}
