/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tservice.Logica.pagos;


/**
 *
 * @author andres
 */
public class ResultadoTransaccion implements java.io.Serializable{

    private String Resultado;
    private int codTransaccion;

    public ResultadoTransaccion() {
    }

    public ResultadoTransaccion(String Resultado, int codTransaccion) {
        this.Resultado = Resultado;
        this.codTransaccion = codTransaccion;
    }

    public String getResultado() {
        return Resultado;
    }

    public void setResultado(String Resultado) {
        this.Resultado = Resultado;
    }

    public int getCodTransaccion() {
        return codTransaccion;
    }

    public void setCodTransaccion(int codTransaccion) {
        this.codTransaccion = codTransaccion;
    }


 

    
}