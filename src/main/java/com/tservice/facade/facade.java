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
import org.hibernate.Session;
import org.hibernate.Transaction;
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
    "inner join intereses interBus on  inter.nombre = interBus.nombre and interBus.eventos_id is not null and interBus.eventos_id <> cal.eventos_id  \n";

    static String sEventosCalificados="select cal.eventos_id from calificacion cal where cal.usuarios_identificacion=:valor and cal.tipo='Evento'  \n " ;
    
        
    static String sNoticiasPreferencia="select interBus.noticias_id from usuarios u \n" +
    "inner join calificacion cal on cal.usuarios_identificacion=u.identificacion and cal.calificacion = 1 \n" +
    "and cal.noticias_id is not null\n and u.identificacion=:valor " +
    "inner join intereses inter on cal.noticias_id = inter.noticias_id \n" +
    "inner join intereses interBus on  inter.nombre = interBus.nombre and interBus.noticias_id is not null interBus.eventos_id <> cal.noticias_id";
    
    static String sNoticiasCalificadas="select cal.noticias_id from calificacion cal where cal.usuarios_identificacion=:valor and cal.tipo='Noticia' \n" ;
    
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
    MensajesCrudFactory mensaCrud;
    @Autowired
    InteresesCrudFactory interesesCrud;
    @Autowired
    App envio;
    @Autowired
    GruposCrudFactory gruposCrud;
    

    
    public Boolean adicionarNoticia(Noticias noticia) throws servergcmExceptions{
      
        int id=Integer.parseInt(String.valueOf(noticiasCrud.count()+1));
        while(noticiasCrud.findOne(id)!=null){
            id+=1;
        }
        noticia.setId(id);
        
        try{
            noticiasCrud.save(noticia);
        }catch(Exception e){
            throw new servergcmExceptions("Error " + e.getMessage());
        }
    
        return true;
    }
    
    
    public Boolean adicionarEvento(Eventos evento) throws servergcmExceptions{
        
                
        int id=Integer.parseInt(String.valueOf(eventosCrud.count()+1));
        while(eventosCrud.findOne(id)!=null){
            id+=1;
        }
        evento.setId(id);
        
        
        try{
            eventosCrud.save(evento);
        }catch(Exception e){
            throw new servergcmExceptions("Error " + e.getMessage());
        }
    
        return true;
    }
    
    
    public Boolean adicionarInteres(Intereses interes) throws servergcmExceptions{
       
       int id=Integer.parseInt(String.valueOf(interesesCrud.count()+1));
        while(interesesCrud.findOne(id)!=null){
            id+=1;
        }
        interes.setId(id);
        
       
        try{
            interesesCrud.save(interes);
        }catch(Exception e){
            return false;
        }
    
        return true;
    }
    
    public Boolean agregarCalificacion(String usuario , String tipo , String idtipo ,Calificacion calificacion) throws servergcmExceptions {
        Usuarios usu=usuCrud.findOne(usuario);
        
        calificacion.setId(Integer.parseInt(String.valueOf(calificacionCrud.count()+1)));
        
        if(usuario==null){
            throw new servergcmExceptions("El usuario identificado con carne N° "+ usuario + " ya se encuentra registrado");
        }
        
        calificacion.setUsuarios(usu);
        
        if(tipo.trim().toUpperCase().equals("EVENTOS")){
            Eventos evento=eventosCrud.findOne(Integer.parseInt(idtipo));
            if(evento==null){
                    throw new servergcmExceptions("El evento  N° "+ idtipo + " ya se encuentra registrado");
            }
            calificacion.setEventos(evento);
             Noticias noticia=noticiasCrud.findOne(2);
            if(noticia==null){
                    throw new servergcmExceptions("La noticia  N° "+ idtipo + " ya se encuentra registrado");
            }
            calificacion.setNoticias(noticia);
        }else{
            Noticias noticia=noticiasCrud.findOne(Integer.parseInt(idtipo));
            if(noticia==null){
                    throw new servergcmExceptions("La noticia  N° "+ idtipo + " ya se encuentra registrado");
            }
            calificacion.setNoticias(noticia);
            Eventos evento=eventosCrud.findOne(2);
            if(evento==null){
                    throw new servergcmExceptions("El evento  N° "+ idtipo + " ya se encuentra registrado");
            }
            calificacion.setEventos(evento);
        
        }
        
        
        calificacionCrud.save(calificacion);
        
        return true;
    }
    
    
    public Boolean login(String carne, String contrasena) throws servergcmExceptions{
        if(!usuCrud.exists(carne))
            throw new servergcmExceptions("El usuario identificado con carne N° "+ carne +" no esta registrado, por favor revice su numero de cerne o registrece para poder acceder a nuestros servicios.");
        
        Usuarios usuarioRegistro = usuCrud.findOne(carne);
        
        if(!contrasena.equals(usuarioRegistro.getPassword()))
            throw new servergcmExceptions("Contrasena incorrecta, por favor verifique.");
        
        return true;
    }
    

        
    public List<Eventos> getEventosUsuario(Usuarios usuario){
        
        List<Eventos> eventos=new ArrayList<Eventos>();
        
        List<String> incluidos=new ArrayList<String>();
        
        System.out.println("Inicio consulta");
        
        //Trae primero eventos segun preferencia
        List<String> res=new ArrayList<String>();
        List<String> resCal=new ArrayList<String>();
        List<String> remove=new ArrayList<String>();
        
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
            
              try {
               resCal = Connect.runQuery(sEventosCalificados.replace(":valor",usuario.getIdentificacion()));
           } catch (ClassNotFoundException ex) {
               Logger.getLogger(facade.class.getName()).log(Level.SEVERE, null, ex);
           } catch (SQLException ex) {
               Logger.getLogger(facade.class.getName()).log(Level.SEVERE, null, ex);
           }
        
            for(String noticia:res){
                if(resCal.contains(noticia)){
                    remove.add(noticia);
                }
            }
            for(String rem:remove){
                res.remove(rem);
            }
        
        }else{
        
            try {
               resCal = Connect.runQuery(sEventosCalificados.replace(":valor",usuario.getIdentificacion()));
           } catch (ClassNotFoundException ex) {
               Logger.getLogger(facade.class.getName()).log(Level.SEVERE, null, ex);
           } catch (SQLException ex) {
               Logger.getLogger(facade.class.getName()).log(Level.SEVERE, null, ex);
           }
        
            for(String noticia:res){
                if(resCal.contains(noticia)){
                    remove.add(noticia);
                }
            }
            for(String rem:remove){
                res.remove(rem);
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
        List<String> resCal=new ArrayList<String>();
        List<String> remove=new ArrayList<String>();
        
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
         
          try {
               resCal = Connect.runQuery(sNoticiasCalificadas.replace(":valor",usuario.getIdentificacion()));
           } catch (ClassNotFoundException ex) {
               Logger.getLogger(facade.class.getName()).log(Level.SEVERE, null, ex);
           } catch (SQLException ex) {
               Logger.getLogger(facade.class.getName()).log(Level.SEVERE, null, ex);
           }
        
            for(String noticia:res){
                if(resCal.contains(noticia)){
                    remove.add(noticia);
                }
            }
            for(String rem:remove){
                res.remove(rem);
            }
        
       }else{
            try {
               resCal = Connect.runQuery(sNoticiasCalificadas.replace(":valor",usuario.getIdentificacion()));
           } catch (ClassNotFoundException ex) {
               Logger.getLogger(facade.class.getName()).log(Level.SEVERE, null, ex);
           } catch (SQLException ex) {
               Logger.getLogger(facade.class.getName()).log(Level.SEVERE, null, ex);
           }
        
            for(String noticia:res){
                if(resCal.contains(noticia)){
                    remove.add(noticia);
                }
            }
            for(String rem:remove){
                res.remove(rem);
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
     

    
    
    /*****************************************************************************/
    

    
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
        
        return true;
    }
    
    public Boolean login(String carne, String contrasena, String regId) throws servergcmExceptions{
        if(!usuCrud.exists(carne))
            throw new servergcmExceptions("El usuario identificado con carne N° "+ carne +" no esta registrado, por favor revice su numero de cerne o registrece para poder acceder a nuestros servicios.");
        
        Usuarios usuarioRegistro = usuCrud.findOne(carne);
        
        if(!contrasena.equals(usuarioRegistro.getPassword()))
            throw new servergcmExceptions("Contrasena incorrecta, por favor verifique.");
        
        usuarioRegistro.setIdentificaciongoogle(regId);
        usuCrud.save(usuarioRegistro);
        
        return true;
    }
    
    public Boolean CrearGrupo(Grupos grupo) throws servergcmExceptions{
        
        if(!usuCrud.exists(grupo.getUsuarios().getCarne()))
            throw new servergcmExceptions("El usuario no existe en la base de datos por favor registrece y vuelva a intentarlo.");
        
        grupo.setEstado(ConstantesServerGcm.estadoActivo);
        
        Usuarios administrador = usuCrud.findOne(grupo.getUsuarios().getCarne());
        grupo.setUsuarios(administrador);
        
        
        if(administrador.getEmail().contains("mail"))
            grupo.setTipoprivado(false);
        else
            grupo.setTipoprivado(true);
        
        Set<Usuarios> usuariosInfoCompleta = new HashSet<>();
        ArrayList<String> RegIds = new ArrayList<>();
        
        for(Usuarios usuario : grupo.getUsuarioses()){
            if(!usuario.getCarne().equals(null) && usuCrud.exists(usuario.getCarne())){
                Usuarios usu = usuCrud.findOne(usuario.getCarne());
                usuariosInfoCompleta.add(usu);
                RegIds.add(usu.getIdentificaciongoogle());
            }
        }
            
        grupo.setUsuarioses(usuariosInfoCompleta);
        grupo.getUsuarioses().add(administrador);
        grupo.setFechacreacion(new Date());
        
        grupo = gruposCrud.save(grupo);
        
        RegIds.add(administrador.getIdentificaciongoogle());
        
        envio.mainCrearGrupo(grupo.getId().toString(), grupo.getNombre(), ConstantesServerGcm.CrearGrupo, RegIds);
        
        return true;
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

    
    public Usuarios adicionarAmigo(String idAmigo) throws servergcmExceptions{
        
        if(!usuCrud.exists(idAmigo))
            throw new servergcmExceptions("La persona identificada con carne n° " + idAmigo + " no esta registrado.");
        
       Usuarios usuario = usuCrud.findOne(idAmigo);
       
       return usuario;
    }
    
    public Boolean envioMensajes(Mensajes mensaje) throws servergcmExceptions{
        
        Boolean resp = false;
        
        if(mensaje.getUsuariosByUsuariodestino()!=null){
            
            if(!usuCrud.exists(mensaje.getUsuariosByUsuariodestino().getCarne()))
                throw new servergcmExceptions("El usuario destino identificado con carne N°"+ mensaje.getUsuariosByUsuariodestino().getCarne() + " no existe.");
            
            Usuarios destino = usuCrud.findOne(mensaje.getUsuariosByUsuariodestino().getCarne());
            Usuarios remitente = usuCrud.findOne(mensaje.getUsuariosByUsuariosorigen().getCarne());
            mensaje.setUsuariosByUsuariodestino(destino);
            mensaje.setUsuariosByUsuariosorigen(remitente);
            
            mensaCrud.save(mensaje);
            
            resp = enviarMensajeUsuario(remitente, destino, mensaje.getContenido());
        }else if(mensaje.getGrupos()!=null){
            
            if(!gruposCrud.exists(mensaje.getGrupos().getId()))
                throw new servergcmExceptions("Este grupo no esta registrado.");
            
            Grupos grupo = gruposCrud.findOne(mensaje.getGrupos().getId());
            Usuarios usuarioOrigen = usuCrud.findOne(mensaje.getUsuariosByUsuariosorigen().getCarne());
            
            mensaje.setGrupos(grupo);
            mensaje.setUsuariosByUsuariosorigen(usuarioOrigen);
            
            mensaCrud.save(mensaje);
            
            resp = enviarMensajeGrupo(grupo, mensaje.getContenido(), mensaje.getUsuariosByUsuariosorigen());
        }else
            throw new servergcmExceptions("El mensaje debe contener un destinatario.");
        
        return resp;
    }
    
    private Boolean enviarMensajeUsuario(Usuarios remitente, Usuarios destino, String mensaje){
        
        ArrayList<String> RegId = new ArrayList<>();
        RegId.add(destino.getIdentificaciongoogle());
        
        envio.main(RegId, remitente.getCarne(), mensaje, ConstantesServerGcm.ClaseMensaje, null);
        
        return true;
        
    }
        
    private Boolean enviarMensajeGrupo(Grupos grupo, String mensaje, Usuarios remitente){
        
        ArrayList<String> RegId = new ArrayList<>();
        Set<Usuarios> destinatarios = grupo.getUsuarioses();
        
        for(Usuarios usuario: destinatarios){
            if(!usuario.getCarne().equals(remitente.getCarne()))
                RegId.add(usuario.getIdentificaciongoogle());
        }
            
        envio.main(RegId, remitente.getCarne(), mensaje, ConstantesServerGcm.ClaseGrupo, grupo.getId().toString());
        
        return true;
        
    }
    
    public List<Usuarios> BusquedaContactos(String keySerch){
        
        
        
        return null;
    }
    
    
    private boolean isNumber(String number){
        try  
        {  
          Integer d = Integer.parseInt(number);  
        }  
        catch(Exception nfe)  
        {  
          return false;  
        }  
        return true;  
    }
    
}
