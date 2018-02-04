/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import java.sql.Date;
import java.util.Calendar;

/**
 *
 * @author JorgeA
 */
public class Cuenta {
    
    private int id;
    private int dniCliente;
    private int idTarifa;
    private Double saldoAcredor;
    private Double saldoDeudor;
    private Date fechaVto;
    private Date fechaVal;

   


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDniCliente() {
        return dniCliente;
    }

    public void setDniCliente(int dniCliente) {
        this.dniCliente = dniCliente;
    }

    public int getIdTarifa() {
        return idTarifa;
    }

    public void setIdTarifa(int idTarifa) {
        this.idTarifa = idTarifa;
    }

    public Double getSaldoAcredor() {
        return saldoAcredor;
    }

    public void setSaldoAcredor(Double saldoAcredor) {
        this.saldoAcredor = saldoAcredor;
    }

    public Double getSaldoDeudor() {
        return saldoDeudor;
    }

    public void setSaldoDeudor(Double saldoDeudor) {
        this.saldoDeudor = saldoDeudor;
    }

    public Date getFechaVto() {
        return fechaVto;
    }

    public void setFechaVto(Date fechaVto) {
        this.fechaVto = fechaVto;
    }

    public Date getFechaVal() {
        return fechaVal;
    }

    public void setFechaVal(Date fechaVal) {
        this.fechaVal = fechaVal;
    }


    @Override
    public String toString() {
        return "Cuenta{" + "id=" + id + ", dniCliente=" + dniCliente + ", idTarifa=" + idTarifa + ", saldoAcredor=" + saldoAcredor + ", saldoDeudor=" + saldoDeudor + ", fechaVto=" + fechaVto + ", fechaVal=" + fechaVal + '}';
    }

  

    


    
    
    
}
