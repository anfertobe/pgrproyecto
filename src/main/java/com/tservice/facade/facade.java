/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tservice.facade;

import com.tservice.Model.*;
import com.tservice.Persistencia.*;
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
    @Autowired
    GruposCrudFactory gruposCrud;
    
    public void Registro(Usuarios nuevoUsuario, Carreras carreraUsuario) throws servergcmExceptions
    {
        if(usuCrud.exists(nuevoUsuario.getCarne())){
            throw new servergcmExceptions("El usuario identificado con carne N° "+ nuevoUsuario.getCarne() + " ya se encuentra registrado");
        }
        Carreras carrera = carreCrud.findOne(carreraUsuario.getId());
        
        if(nuevoUsuario.getEmail().contains("mail"))
            nuevoUsuario.setPerfil(ConstantesServerGcm.Estudiante);
        else
            nuevoUsuario.setPerfil(ConstantesServerGcm.Administrativo);
        
        Set<Usuarios> usuariosDeCarrera = carrera.getUsuarioses();
        usuariosDeCarrera.add(nuevoUsuario);
        carrera.setUsuarioses(usuariosDeCarrera);
        
        envio.main(nuevoUsuario.getIdentificaciongoogle());
        
        carreCrud.save(carrera);
        
        nuevoUsuario.getCarrerases().add(carrera);
        
        usuCrud.save(nuevoUsuario);
    }
    
    public void login(String carne, String contraseña) throws servergcmExceptions{
        if(!usuCrud.exists(carne))
            throw new servergcmExceptions("El usuario identificado con carne N° "+ carne +" no esta registrado, por favor revice su numero de cerne o registrece para poder acceder a nuestros servicios.");
        
        Usuarios usuarioRegistro = usuCrud.findOne(carne);
        
        if(!contraseña.equals(usuarioRegistro.getContraseña()))
            throw new servergcmExceptions("Contraseña incorrecta, por favor verifique.");
    }
    
    public int CrearGrupo(String nombre,Usuarios admin) throws servergcmExceptions{
        
        if(!usuCrud.exists(admin.getCarne()))
            throw new servergcmExceptions("El usuario no existe en la base de datos por favor registrece y vuelva a intentarlo.");
        
        Usuarios administrador = usuCrud.findOne(admin.getCarne());
        
        
        
        Grupos nuevoGrupo = new Grupos();
        nuevoGrupo.setNombre(nombre);
        nuevoGrupo.setEstado(ConstantesServerGcm.estadoActivo);
        nuevoGrupo.setUsuarios(administrador);
        nuevoGrupo.setFechacreacion(new Date());
        
        if(administrador.getEmail().contains("mail"))
            nuevoGrupo.setTipoprivado(false);
        else
            nuevoGrupo.setTipoprivado(true);
        
        nuevoGrupo.getUsuarioses().add(admin);
                
        admin.getGruposes_1().add(nuevoGrupo);
        gruposCrud.save(nuevoGrupo);
        
        
        
        return nuevoGrupo.getId();
    }
    
    public void AdiciaonarUsuariosGrupo(Grupos grupo, Usuarios usuario) throws servergcmExceptions{
        if(!gruposCrud.exists(grupo.getId()))
            throw new servergcmExceptions("El grupo al cual deseas adicionar amigos no existe");
        
        if(!usuCrud.exists(usuario.getCarne()))
            throw new servergcmExceptions("El amigo al cual deseas adicionar al grupo no esta registrado.");
        
        Grupos group = gruposCrud.findOne(grupo.getId());
        Usuarios amigo = usuCrud.findOne(usuario.getCarne());
        
        if(group.getUsuarioses().contains(amigo))
            throw new servergcmExceptions("Este usuario ya esta inscrito al grupo.");
        
        group.getUsuarioses().add(amigo);
        
        gruposCrud.save(group);
    }
    
    public void pruebaRapida(String usuario, String CodigoGoogle){
        envio.main(CodigoGoogle);
    }
            
    
}
