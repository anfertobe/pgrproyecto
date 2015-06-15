/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tservice.Logica.pasadoJudicial;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author LuisAndres
 */
public class PasadoJudicial {
 
    private int idPersona;
    
    private String documento;
    
    private String descripcion;
    
    private List<Antecedente> antecedentes = new LinkedList<>();

    public PasadoJudicial(){
        
    }
    
    public PasadoJudicial(String descripcion,Date fechaAntecedente,List<Antecedente> antecedentes){
        this.descripcion=descripcion;
        this.antecedentes=antecedentes;
    }
    
    public PasadoJudicial(String descripcion,Date fechaAntecedente){
        this.descripcion=descripcion;
    }
    
    public int getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(int idPersona) {
        this.idPersona = idPersona;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }
      
    public List<Antecedente> getAntecedentes(){
        return antecedentes;
    }
    
    public void setAntecedentes(ArrayList<Antecedente> antecedentes){
        this.antecedentes=antecedentes;
    }
    
    public String getDescripcion(){
        return descripcion;
    }
    
    public void setDescripcion(String descripcion){
        this.descripcion=descripcion;
    }
}
