/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tservice.Logica.pagos;

import java.util.Date;

/**
 *
 * @author andres
 */
public class ConsultaTransaccion {
    
    private int codigoTransaccion;
    private String estado;
    private int montoTransferido;
    private String fechaRealizacion;

    public ConsultaTransaccion() {
    }

    public ConsultaTransaccion(int codigoTransaccion, String estado, int montoTransferido, String fechaRealizacion) {
        this.codigoTransaccion = codigoTransaccion;
        this.estado = estado;
        this.montoTransferido = montoTransferido;
        this.fechaRealizacion = fechaRealizacion;
    }

    public int getCodigoTransaccion() {
        return codigoTransaccion;
    }

    public void setCodigoTransaccion(int codigoTransaccion) {
        this.codigoTransaccion = codigoTransaccion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getMontoTransferido() {
        return montoTransferido;
    }

    public void setMontoTransferido(int montoTransferido) {
        this.montoTransferido = montoTransferido;
    }

    public String getFechaRealizacion() {
        return fechaRealizacion;
    }

    public void setFechaRealizacion(String fechaRealizacion) {
        this.fechaRealizacion = fechaRealizacion;
    }
    
    
}