/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tservice.facade;

import com.tservice.BD.Connect;
import com.tservice.Model.*;
import com.tservice.Persistencia.*;
import com.tservice.exceptions.servergcmExceptions;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

/**
 *
 * @author andres
 */
@Service
public class facade {
    
    private Session session;
    private Transaction tx;
    static String sNoticiasDefault="select interNot.noticias_id from usuarios u \n" +
    "join carreras_has_usuarios c on c.usuarios_carne=u.carne\n and u.identificacion=:valor " +
    "join carreras car on c.carreras_id=car.id \n" +
    "join intereses inter on inter.carreras_id=car.id \n" +
    "join intereses interNot on inter.nombre=interNot.nombre and interNot.noticias_id is not null";
    
    static String sEventosDefault="select interEv.eventos_id from usuarios u \n" +
    "join carreras_has_usuarios c on c.usuarios_carne=u.carne\n and u.identificacion=:valor " +
    "join carreras car on c.carreras_id=car.id\n" +
    "join intereses inter on inter.carreras_id=car.id \n " +
    "join intereses interEv on inter.nombre=interEv.nombre and  interEv.eventos_id  is not null";
        
    static String sEventosPreferencia="select interBus.eventos_id from usuarios u \n" +
    "inner join calificacion cal on cal.usuarios_identificacion=u.identificacion and cal.calificacion = 1 \n" +
    "and cal.eventos_id is not null\n and u.identificacion=:valor " +
    "inner join intereses inter on cal.eventos_id = inter.eventos_id \n" +
    "inner join intereses interBus on  inter.nombre = interBus.nombre and interBus.eventos_id is not null \n";

    static String sNoticiasPreferencia="select interBus.noticias_id from usuarios u \n" +
    "inner join calificacion cal on cal.usuarios_identificacion=u.identificacion and cal.calificacion = 1 \n" +
    "and cal.noticias_id is not null\n and u.identificacion=:valor " +
    "inner join intereses inter on cal.noticias_id = inter.noticias_id \n" +
    "inner join intereses interBus on  inter.nombre = interBus.nombre and interBus.noticias_id is not null";
    
    
    @Autowired
    UsuariosCrudFactory usuCrud;
    @Autowired
    CarrerasCrudFactory carreCrud;
    @Autowired
    EventosCrudFactory eventosCrud;
    @Autowired
    NoticiasCrudFactory noticiasCrud;
    @Autowired
    CalificacionCrudFactory calificacionCrud;
    @Autowired
    InteresesCrudFactory interesesCrud;
    @Autowired
    App envio;
    @Autowired
    GruposCrudFactory gruposCrud;
    
    public boolean registroMomentaneo(String Usuario, String RegId) throws servergcmExceptions{
         if(usuCrud.exists(Usuario)){
            throw new servergcmExceptions("El usuario identificado con carne N° "+ Usuario + " ya se encuentra registrado");
        }
         
         envio.main(RegId, ConstantesServerGcm.TituloMensaje, ConstantesServerGcm.ContenidoMensaje);
         
         Usuarios usuario = new Usuarios();
         usuario.setIdentificaciongoogle(RegId);
         usuario.setCarne(Usuario);
         
         usuCrud.save(usuario);
         
         return true;
    }
    
    public Boolean adicionarNoticia(Noticias noticia) throws servergcmExceptions{
    
        try{
            noticiasCrud.save(noticia);
        }catch(Exception e){
            return false;
        }
    
        return true;
    }
    
    
    public Boolean adicionarEvento(Eventos evento) throws servergcmExceptions{
       
        try{
            eventosCrud.save(evento);
        }catch(Exception e){
            return false;
        }
    
        return true;
    }
    
    
    public Boolean adicionarInteres(Intereses interes) throws servergcmExceptions{
       
        try{
            interesesCrud.save(interes);
        }catch(Exception e){
            return false;
        }
    
        return true;
    }
    
    public Boolean agregarCalificacion(Calificacion calificacion) throws servergcmExceptions {
        try{
            calificacionCrud.save(calificacion);
        }catch(Exception e){
            return false;
        }
    
        return true;
    }
    
    
    
    public Boolean Registro(Usuarios nuevoUsuario, Carreras carreraUsuario) throws servergcmExceptions
    {
        
        
        if(usuCrud.exists(nuevoUsuario.getCarne())){
            throw new servergcmExceptions("El usuario identificado con carne N° "+ nuevoUsuario.getCarne() + " ya se encuentra registrado");
        }
        Carreras carrera = carreCrud.findOne(carreraUsuario.getId());
        
        if(nuevoUsuario.getEmail().contains("mail"))
            nuevoUsuario.setPerfil(ConstantesServerGcm.Estudiante);
        else
            nuevoUsuario.setPerfil(ConstantesServerGcm.Administrativo);
       
        nuevoUsuario.getCarrerases().add(carrera);
        
        usuCrud.save(nuevoUsuario);
        
        envio.main(nuevoUsuario.getIdentificaciongoogle(), ConstantesServerGcm.TituloMensaje, ConstantesServerGcm.ContenidoMensaje);
        
        return true;
    }
    
    public Boolean login(String carne, String contraseña) throws servergcmExceptions{
        if(!usuCrud.exists(carne))
            throw new servergcmExceptions("El usuario identificado con carne N° "+ carne +" no esta registrado, por favor revice su numero de cerne o registrece para poder acceder a nuestros servicios.");
        
        Usuarios usuarioRegistro = usuCrud.findOne(carne);
        
        if(!contraseña.equals(usuarioRegistro.getPassword()))
            throw new servergcmExceptions("Contraseña incorrecta, por favor verifique.");
        
        return true;
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
        envio.main(CodigoGoogle, ConstantesServerGcm.TituloMensaje, ConstantesServerGcm.ContenidoMensaje);
    }
    
    public Usuarios adicionarAmigo(String idAmigo) throws servergcmExceptions{
        
        if(!usuCrud.exists(idAmigo))
            throw new servergcmExceptions("La persona identificada con carne n° " + idAmigo + " no esta registrado.");
        
       Usuarios usuario = usuCrud.findOne(idAmigo);
       
       return usuario;
    }
    
    public Boolean envioMensajes(Mensajes mensaje) throws servergcmExceptions{
        
        Boolean resp = false;
        
        if(!Objects.isNull(mensaje.getUsuariosByUsuariodestino())){
            
            if(!usuCrud.exists(mensaje.getUsuariosByUsuariodestino().getCarne()))
                throw new servergcmExceptions("El usuario destin identificado con carne N°"+ mensaje.getUsuariosByUsuariodestino().getCarne() + " no existe.");
            
            Usuarios destino = usuCrud.findOne(mensaje.getUsuariosByUsuariodestino().getCarne());
            
            mensaje.setUsuariosByUsuariodestino(destino);
            
            resp = enviarMensajeUsuario(destino, mensaje.getContenido());
        }else if(!Objects.isNull(mensaje.getGrupos())){
            resp = enviarMensajeGrupo(mensaje.getGrupos(), mensaje.getContenido());
        }else
            throw new servergcmExceptions("El mensaje debe contener un destinatario.");
        return resp;
    }
        
    public List<Eventos> getEventosUsuario(Usuarios usuario){
        
        List<Eventos> eventos=new ArrayList<Eventos>();
        
        List<String> incluidos=new ArrayList<String>();
        
        System.out.println("Inicio consulta");
        
        //Trae primero eventos segun preferencia
        List<String> res=new ArrayList<String>();
        try {
            res = Connect.runQuery(sEventosPreferencia.replace(":valor",usuario.getIdentificacion()));
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(facade.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(facade.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Obtiene lista size "+res.size());
        
        
        
        //El usuario no tiene preferencias traer default
        if (res.isEmpty()){
            try {
                res=Connect.runQuery(sEventosDefault.replace(":valor",usuario.getIdentificacion()));
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(facade.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(facade.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
         
        for(String evento:res){
            Eventos ev=eventosCrud.findOne(Integer.parseInt(evento));
            if(ev!=null){
                if(!incluidos.contains(evento)){
                    eventos.add(ev);
                    incluidos.add(evento);
                }
            }
        }
        
        return eventos;
    }
        
    public List<Noticias> getNoticiasUsuario(Usuarios usuario){
        
        List<Noticias> noticias=new ArrayList<Noticias>();
              
        List<String> incluidos=new ArrayList<String>();
        
        //Trae primero noticias segun preferencia
        List<String> res=new ArrayList<String>();
        
         try {
            res = Connect.runQuery(sNoticiasPreferencia.replace(":valor",usuario.getIdentificacion()));
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(facade.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(facade.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        
        //El usuario no tiene preferencias traer default
        if (res.isEmpty()){
         try {
            res = Connect.runQuery(sNoticiasDefault.replace(":valor",usuario.getIdentificacion()));
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(facade.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(facade.class.getName()).log(Level.SEVERE, null, ex);
            }
       }
         
        for(String noticia:res){
            Noticias not=noticiasCrud.findOne(Integer.parseInt(noticia));
            if(not!=null){
                if(!incluidos.contains(noticia)){
                        noticias.add(not);
                        incluidos.add(noticia);
                }
            }
        }
        
               
        return noticias;
    }
    
    private Boolean enviarMensajeUsuario(Usuarios usuario, String mensaje){
        
        envio.main(usuario.getIdentificaciongoogle(), usuario.getCarne(), mensaje);
        
        return true;
        
    }
        
    private Boolean enviarMensajeGrupo(Grupos grupo, String mensaje){
        
        return true;
        
    }
    
}
