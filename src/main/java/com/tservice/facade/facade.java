/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tservice.facade;

import com.tservice.Model.*;
import com.tservice.Persistencia.CarrerasCrudFactory;
import com.tservice.Persistencia.UsuariosCrudFactory;
import com.tservice.exceptions.servergcmExceptions;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author andres
 */
@Service
public class facade {
    
    @Autowired
    UsuariosCrudFactory usuCrud;
    @Autowired
    CarrerasCrudFactory carreCrud;
    @Autowired
    App envio;
    
    public void Registro(Usuarios nuevoUsuario, Carreras carreraUsuario) throws servergcmExceptions
    {
        if(!usuCrud.exists(nuevoUsuario.getCarne())){
            throw new servergcmExceptions("El usuario identificado con carne N° "+ nuevoUsuario.getCarne() + " ya se encuentra registrado");
        }
        Carreras carrera = carreCrud.findOne(carreraUsuario.getId());
        Set<Usuarios> usuariosDeCarrera = carrera.getUsuarioses();
        usuariosDeCarrera.add(nuevoUsuario);
        carrera.setUsuarioses(usuariosDeCarrera);
        
        carreCrud.save(carrera);
        
        nuevoUsuario.getCarrerases().add(carrera);
        
        usuCrud.save(nuevoUsuario);
    }
    
    public void login(int carne, String contraseña) throws servergcmExceptions{
        if(!usuCrud.exists(carne))
            throw new servergcmExceptions("El usuario identificado con carne N° "+ carne +" no esta registrado, por favor revice su numero de cerne o registrece para poder acceder a nuestros servicios.");
        
        Usuarios usuarioRegistro = usuCrud.findOne(carne);
        
        if(!contraseña.equals(usuarioRegistro.getContraseña()))
            throw new servergcmExceptions("Contraseña incorrecta, por favor verifique.");
    }
    
    public void pruebaRapida(String usuario, String CodigoGoogle){
        envio.main(CodigoGoogle);
    }
            
    
}
