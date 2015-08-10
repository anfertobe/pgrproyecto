
package com.tservice.restcontrollersserver;


import com.tservice.Model.*;
import com.tservice.Persistencia.*;
import com.tservice.exceptions.servergcmExceptions;
import com.tservice.facade.facade;
import java.util.*;
import java.util.logging.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

/**
 * @author Andres Torres y Luis Gomez
 */
@RestController
@RequestMapping("/servergcm")
public class RestControllerGcm {
    
    @Autowired
    UbicacionCrudFactory ubiCrud;
    @Autowired
    CarrerasCrudFactory carrecrud;
    @Autowired
    EventosCrudFactory evenCrud;
    @Autowired
    MensajesCrudFactory mensaCrud;
    @Autowired
    GruposCrudFactory grupoCrud;
    @Autowired
    UsuariosCrudFactory usuaCrud;
    @Autowired
    NoticiasCrudFactory noticiasCrud;
    @Autowired
    CalificacionCrudFactory calificacionCrud;
    @Autowired
    InteresesCrudFactory interesesCrud;
    @Autowired
    facade fachada;
    
    @RequestMapping(value="/evento",method = RequestMethod.PUT)
     public ResponseEntity<?> adicionarEvento(@RequestBody Eventos evento){
         boolean result = false;
             
             try{
                result=fachada.adicionarEvento(evento);
             }catch(Exception e){
                 return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
            }
                         
         return new ResponseEntity<>(result, HttpStatus.ACCEPTED);
     }
    
     
    @RequestMapping(value="/carrera",method = RequestMethod.PUT)
     public ResponseEntity<?> aadCarreras(@RequestBody Carreras carrera){
             
            carrecrud.save(carrera);
                         
         return new ResponseEntity<>(HttpStatus.ACCEPTED);
     } 
      
    @RequestMapping(value="/mensajes",method = RequestMethod.GET)        
    public List<Mensajes> consultarMensajes()  throws ResourceNotFoundException { 
        
        List<Mensajes> edificios = new LinkedList<Mensajes>();
        
        for(Mensajes edif : mensaCrud.findAll())
            edificios.add(edif);
        
          return edificios;
    }
    
    
    @RequestMapping(value="/mensajes/{usuario}",method = RequestMethod.GET)        
    public List<Mensajes> consultarMensajes(@PathVariable("usuario") String usuario)  throws ResourceNotFoundException { 
        
        List<Mensajes> edificios = new LinkedList<Mensajes>();
        
        for(Mensajes edif : mensaCrud.findAll())
            if(edif.getUsuariosByUsuariodestino()!=null && edif.getUsuariosByUsuariodestino().getCarne().equals(usuario)){
                edificios.add(edif);
            }
        
          return edificios;
    }
    
    
    
            @RequestMapping(value="/grupos",method = RequestMethod.GET)        
    public List<Grupos> consultarGrupos()  throws ResourceNotFoundException { 
        
        List<Grupos> edificios = new LinkedList<Grupos>();
        
        for(Grupos edif : grupoCrud.findAll())
            edificios.add(edif);
        
          return edificios;
    }
    
            @RequestMapping(value="/usuarios",method = RequestMethod.GET)        
    public List<Usuarios> consultarUsuarios()  throws ResourceNotFoundException { 
        
        List<Usuarios> edificios = new LinkedList<Usuarios>();
        
        for(Usuarios edif : usuaCrud.findAll())
            edificios.add(edif);
        
          return edificios;
    }
    
    
     
    @RequestMapping(value="/calificacion/{usuario}/{tipo}/{idTipo}",method = RequestMethod.PUT)
     public ResponseEntity<?> adicionarCalificacion(@RequestBody Calificacion calificacion,@PathVariable("usuario") String usuario,@PathVariable("tipo") String tipo,@PathVariable("idTipo") String idTipo){
         boolean result = false;
             
             try{
                fachada.agregarCalificacion(usuario,tipo,idTipo,calificacion);
             }catch(Exception e){
                 return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
            }
                         
         return new ResponseEntity<>(HttpStatus.ACCEPTED);
     }
    
     
     
    @RequestMapping(value="/calificaciones",method = RequestMethod.GET)
     public List<Calificacion> getCalificaciones(){
             
        List<Calificacion> edificios = new LinkedList<Calificacion>();
        
        for(Calificacion edif : calificacionCrud.findAll())
            edificios.add(edif);
        
          return edificios;
     }
    
     
     
    @RequestMapping(value="/interes",method = RequestMethod.PUT)
     public ResponseEntity<?> adicionarInteres(@RequestBody Intereses interes){
         boolean result = false;
             
             try{
             result=fachada.adicionarInteres(interes);
             }catch(Exception e){
                 return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
            }
                         
         return new ResponseEntity<>(result, HttpStatus.ACCEPTED);
     }
    
    
    @RequestMapping(value="/noticia",method = RequestMethod.PUT)
     public ResponseEntity<?> adicionarNoticia(@RequestBody Noticias noticia){
         boolean result = false;
             
             try{
                                  
             result=fachada.adicionarNoticia(noticia);
             }catch(Exception e){
                 return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
            }
                         
         return new ResponseEntity<>(result, HttpStatus.ACCEPTED);
     } 
     
     @RequestMapping(value="/eventos/{usuario}",method = RequestMethod.GET)
     public List<Eventos> getEventosUsuario(@PathVariable("usuario") String usuario){
         
         List<Eventos> ev=new ArrayList<Eventos>();
         
         Usuarios usu=usuaCrud.findOne(usuario);
        
         try{
             return fachada.getEventosUsuario(usu);
         }catch(Exception e){
             Eventos eve=evenCrud.findOne(2);
             eve.setDescripcion(e.getMessage());
             ev.add(eve);
             return ev;
         }
     }

     
     @RequestMapping(value="/noticias/{usuario}",method = RequestMethod.GET)
     public  List<Noticias> getNoticiasUsuario(@PathVariable("usuario") String usuario){
         
         
         Usuarios usu=usuaCrud.findOne(usuario);
         
         try{
             return fachada.getNoticiasUsuario(usu);
         }catch(Exception e){
             return null;
         }
     }

    
     @RequestMapping(value="/login/{usuario}/{password}",method = RequestMethod.GET)
     public ResponseEntity<?> login(@PathVariable("usuario") String usuario, @PathVariable("password") String password){
         
         try{
             fachada.login(usuario, password);
         }catch(Exception e){
             return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
         }
         
         return new ResponseEntity<>(HttpStatus.ACCEPTED);
     }

    @RequestMapping(value="/Ubicaciones",method = RequestMethod.GET)        
    public List<Ubicacion> consultarCalificacion()  throws ResourceNotFoundException { 
        
        List<Ubicacion> Ubicaciones = new LinkedList<Ubicacion>();
        
        for(Ubicacion edif : ubiCrud.findAll())
            Ubicaciones.add(edif);
        
          return Ubicaciones;
    }
        
            @RequestMapping(value="/eventos",method = RequestMethod.GET)        
    public List<Eventos> consultarEventos()  throws ResourceNotFoundException { 
        
        List<Eventos> edificios = new LinkedList<Eventos>();
        
        for(Eventos edif : evenCrud.findAll())
            edificios.add(edif);
        
          return edificios;
    }
    
          
    @RequestMapping(value="/noticias",method = RequestMethod.GET)        
    public List<Noticias> consultarNoticias()  throws ResourceNotFoundException { 
        
        List<Noticias> edificios = new LinkedList<Noticias>();
        
        for(Noticias edif : noticiasCrud.findAll())
            edificios.add(edif);
        
          return edificios;
    }
    
    @RequestMapping(value="/intereses",method = RequestMethod.GET)        
    public List<Intereses> consultarIntereses()  throws ResourceNotFoundException { 
        
        List<Intereses> edificios = new LinkedList<Intereses>();
        
        for(Intereses edif : interesesCrud.findAll())
            edificios.add(edif);
        
          return edificios;
    }
    
    
 
    /****************************************************************************/
    
    @RequestMapping(value="/mensaje",method = RequestMethod.PUT)        
    public ResponseEntity<?> Mensaje(@RequestBody Mensajes mensaje)  throws ResourceNotFoundException { 
        boolean result = false;
        
        try {
            result = fachada.envioMensajes(mensaje);
        } catch (servergcmExceptions ex) {
            Logger.getLogger(RestControllerGcm.class.getName()).log(Level.SEVERE, null, ex);
        }
        
          return new ResponseEntity<>(result, HttpStatus.ACCEPTED);
    }
    
    
    @RequestMapping(value="/adicion/grupo",method = RequestMethod.POST)
     public ResponseEntity<?> adicionarGrupo(@RequestBody Grupos grupo) {
            
         Boolean respuesta = false;

        try {
            respuesta = fachada.CrearGrupo(grupo);
        } catch (servergcmExceptions ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
         
         return new ResponseEntity<>(respuesta, HttpStatus.ACCEPTED);
     }
    
      @RequestMapping(value="/usuario/{carne}",method = RequestMethod.GET)        
    public List<Usuarios> consultarUsuario(@PathVariable("carne") String carne )  throws ResourceNotFoundException { 

          return usuaCrud.search(carne);
    }
    
    @RequestMapping(value="/contactos/{carne}",method = RequestMethod.GET)        
    public List<Usuarios> consultarContactos(@PathVariable("carne") String carne )  throws ResourceNotFoundException { 

          return usuaCrud.searchContactos(carne);
    }
    
    @RequestMapping(value="/grupos/{carne}",method = RequestMethod.GET)        
    public List<Grupos> consultargrupos(@PathVariable("carne") String carne )  throws ResourceNotFoundException { 

          return grupoCrud.searchGrupos(carne);
    }
    
      
    @RequestMapping(value="/carreras",method = RequestMethod.GET)        
    public List<Carreras> consultarcarreras()  throws ResourceNotFoundException { 
          return carrecrud.searchcarreras();
    }
     
    @RequestMapping(value="/adicion/{idAmgo}",method = RequestMethod.POST)
     public Usuarios adicionarAmigo(@PathVariable("idAmgo") String idAmgo) throws servergcmExceptions{
            
         Usuarios usuario = null;
         

             usuario = fachada.adicionarAmigo(idAmgo);
         
         
         return usuario;
     }

    
     @RequestMapping(value="/login/{usuario}/{password}/{regId}",method = RequestMethod.GET)
     public ResponseEntity<?> login(@PathVariable("usuario") String usuario, @PathVariable("password") String password, @PathVariable("regId") String regId){
         boolean respuesta = false;
         
         try{
             respuesta = fachada.login(usuario, password, regId);
         }catch(Exception e){
             return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
         }
         
         return new ResponseEntity<>(respuesta,HttpStatus.ACCEPTED);
     }
     
     @RequestMapping(value="/registro/{carrera}",method = RequestMethod.PUT)
     public ResponseEntity<?> registro(@RequestBody Usuarios usuario, @PathVariable("carrera") Integer idCarrera){
         
         Carreras carrera = carrecrud.findOne(idCarrera);
         
         boolean respuesta = false;
         
         try{
             respuesta = fachada.Registro(usuario, carrera);
         }catch(Exception e){
             return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
         }
         
         return new ResponseEntity<>(respuesta, HttpStatus.ACCEPTED);
     }

            @RequestMapping(value="/pruebaConsultas/{keySearch}",method = RequestMethod.GET)        
    public List<Usuarios> SearchContactos(@PathVariable("keySearch") String keySearch)  throws ResourceNotFoundException { 
                  return usuaCrud.pruebaQuery("%"+keySearch.toLowerCase()+"%");
    }

     
     
    
}