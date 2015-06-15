/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tservice.Logica.pasadoJudicial;

import java.util.Date;

/**
 *
 * @author LuisAndres
 */
public class Antecedente {
   
    private int idAntecedente;
    private String documento;
    private String fecha;
    private String descripcion;
    
    public Antecedente(){
        
    }
    
    public Antecedente(int idAntecedente,String documento,String descripcion,String fechaAntecedente){
        this.descripcion=descripcion;
        this.fecha=fechaAntecedente;
        this.idAntecedente=idAntecedente;   
        this.documento =documento;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getIdAntecedente() {
        return idAntecedente;
    }

    public void setIdAntecedente(int idAntecedente) {
        this.idAntecedente = idAntecedente;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }
    
    public String getDescripcion(){
        return descripcion;
    }
    
    public void setDescripcion(String descripcion){
        this.descripcion=descripcion;
    }
       
}
