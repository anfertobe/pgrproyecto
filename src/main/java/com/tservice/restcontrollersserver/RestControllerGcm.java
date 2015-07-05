
package com.tservice.restcontrollersserver;


import com.tservice.Model.*;
import com.tservice.Persistencia.*;
import com.tservice.exceptions.servergcmExceptions;
import com.tservice.facade.facade;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
    
     
     
    @RequestMapping(value="/mensaje",method = RequestMethod.POST)
     public ResponseEntity<?> adicionarAmigo(@RequestBody Mensajes mensaje){
         
         boolean result = false;
         
         try{
             result = fachada.envioMensajes(mensaje);
         }catch(Exception e){
             return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
         }
         
         return new ResponseEntity<>(result, HttpStatus.ACCEPTED);
     }
    
    @RequestMapping(value="/adicion/{idAmgo}",method = RequestMethod.POST)
     public Usuarios adicionarAmigo(@PathVariable("idAmgo") String idAmgo) throws servergcmExceptions{
            
         Usuarios usuario = null;
         

             usuario = fachada.adicionarAmigo(idAmgo);
         
         
         return usuario;
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

          @RequestMapping(value="/registromomentaneo/{usuario}/{regid}",method = RequestMethod.PUT)
     public ResponseEntity<?> registroMomentane(@PathVariable("usuario") String usuario, @PathVariable("regid") String regid){

         try{
             fachada.registroMomentaneo(usuario, regid);
         }catch(Exception e){
             return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
         }
         
         return new ResponseEntity<>(HttpStatus.ACCEPTED);
     }
     
     @RequestMapping(value="/registro/{carrera}",method = RequestMethod.PUT)
     public ResponseEntity<?> registro(@RequestBody Usuarios usuario, @PathVariable("carrera") Integer idCarrera){
         
         Carreras carrera = carrecrud.findOne(idCarrera);
         
         try{
             fachada.Registro(usuario, carrera);
         }catch(Exception e){
             return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
         }
         
         return new ResponseEntity<>(HttpStatus.ACCEPTED);
     }
    
    @RequestMapping(value="/envio/{usuario}/{idgoogle}",method = RequestMethod.POST)        
    public ResponseEntity<?> envioPrueba(@PathVariable("usuario") String usuario, @PathVariable("idgoogle") String idgoogle)  throws ResourceNotFoundException { 
        
        fachada.pruebaRapida(usuario, idgoogle);
        
          return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
    
    @RequestMapping(value="/Ubicaciones",method = RequestMethod.GET)        
    public List<Ubicacion> consultarCalificacion()  throws ResourceNotFoundException { 
        
        List<Ubicacion> Ubicaciones = new LinkedList<Ubicacion>();
        
        for(Ubicacion edif : ubiCrud.findAll())
            Ubicaciones.add(edif);
        
          return Ubicaciones;
    }
    

    
            @RequestMapping(value="/carreras",method = RequestMethod.GET)        
    public List<Carreras> consultarCarreras()  throws ResourceNotFoundException { 
        
        List<Carreras> edificios = new LinkedList<Carreras>();
        
        for(Carreras edif : carrecrud.findAll())
            edificios.add(edif);
        
          return edificios;
    }
    
            @RequestMapping(value="/eventos",method = RequestMethod.GET)        
    public List<Eventos> consultarEventos()  throws ResourceNotFoundException { 
        
        List<Eventos> edificios = new LinkedList<Eventos>();
        
        for(Eventos edif : evenCrud.findAll())
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
    
    
    
    
    @RequestMapping(value="/mensajes",method = RequestMethod.GET)        
    public List<Mensajes> consultarMensajes()  throws ResourceNotFoundException { 
        
        List<Mensajes> edificios = new LinkedList<Mensajes>();
        
        for(Mensajes edif : mensaCrud.findAll())
            edificios.add(edif);
        
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
    
    
/*    
    @Autowired
    PersistenceFacede persistenci;
    
    
    
    @RequestMapping(value="/Calificacion",method = RequestMethod.GET)        
    public List<Calificacion> consultarCalificacion()  throws ResourceNotFoundException { 
          return persistenci.traerCalificaciones();
    }
    
    @RequestMapping(value="/Postulantes",method = RequestMethod.GET)        
    public List<Postulante> consultarPostulantes()  throws ResourceNotFoundException { 
          return persistenci.traerPostulantes();
    }
    
    @RequestMapping(value="/Categorias",method = RequestMethod.GET)        
    public List<Categoria> consultarCategorias() { 
          return persistenci.traerCategorias();
    }
    
    @RequestMapping(value="/Interes",method = RequestMethod.GET)        
    public List<Interes> consultarIntereses()  throws ResourceNotFoundException { 
          return persistenci.traerIntereses();
    }
    
    @RequestMapping(value="/Interes",method = RequestMethod.PUT)        
    public ResponseEntity<?> agregarInteres(@RequestBody Postulante postulante){ 
        
        try {
            //System.out.println("Este es el postulante: "+interes.getIdentificacion().getNombre());
            
            persistenci.uddPostulante(postulante);
        } catch (tserviceExceptions ex) {
                        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
    
    @RequestMapping(value="/Calificacion/{IdOferta}",method = RequestMethod.PUT)        
    public ResponseEntity<?> agregarCalificacion(@RequestBody Calificacion calificacion ,@PathVariable("IdOferta") int idOferta){ 
        
        Oferta oferta = oferCru.findOne(idOferta);
       
       if(oferta == null)
           return new ResponseEntity<>("La oferta No Existe", HttpStatus.INTERNAL_SERVER_ERROR);
         try {
            persistenci.addCalificacion(oferta,calificacion);
        } catch (tserviceExceptions ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
       return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
    
    
   
    @RequestMapping(value="Categorias/{idCategoria}",method = RequestMethod.GET)
    public Categoria consultarCategoria(@PathVariable("idCategoria") int idCategoria) throws ResourceNotFoundException {
        return persistenci.consultarCategoria(idCategoria);
    }
    
    @RequestMapping(value="Postulantes/{idPostulante}",method = RequestMethod.GET)
    public Postulante consultarPostulante(@PathVariable("idPostulante") int idPosultante) throws ResourceNotFoundException {
        return persistenci.consultarPostulante(idPosultante);
    }
    
    @RequestMapping(value="/Postulantes",method = RequestMethod.PUT)        
    public ResponseEntity<?> agregarPostulante(@RequestBody Postulante postulante){ 
        
        try {
            hojadevidacrud.save(postulante.getHojaDeVida());
            persistenci.addPostulante(postulante);
        } catch (tserviceExceptions ex) {
                        hojadevidacrud.delete(postulante.getHojaDeVida());
                       return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
    
       
    @RequestMapping(value="/Categorias",method = RequestMethod.PUT)        
    public ResponseEntity<?> agregarCategoria(@RequestBody Categoria categoria){ 
        
        try {
            persistenci.addCategoria(categoria);
        } catch (tserviceExceptions ex) {
                     return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
    
        @RequestMapping(value="/Publicantes",method = RequestMethod.GET)        
    public List<Publicante> consultarPublicantes()  throws ResourceNotFoundException { 
          return persistenci.traerPublicantes();
    }    
    
    @RequestMapping(value="Publicantes/{idPublicante}",method = RequestMethod.GET)
    public Publicante consultarPublicante(@PathVariable("idPublicante") int idPublicante) throws ResourceNotFoundException {
        return persistenci.consultarPublicante(idPublicante);
    }
    
    @RequestMapping(value="/Publicantes",method = RequestMethod.PUT)        
    public ResponseEntity<?> agregarPublicante(@RequestBody Publicante publicante)  throws ResourceNotFoundException{ 
     
        try {
            persistenci.addPublicante(publicante);
        } catch (tserviceExceptions ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
       return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
  
    
    @RequestMapping(value="/Ofertas",method = RequestMethod.GET)        
    public List<Oferta> consultarOfertas()  throws ResourceNotFoundException { 
          return persistenci.traerOfertas();
    }
    
    @RequestMapping(value="Ofertas/{idOferta}",method = RequestMethod.GET)
    public Oferta consultarOferta(@PathVariable("idOferta") int idOferta) throws ResourceNotFoundException {
        return persistenci.consultarOferta(idOferta);
    }
    
    @RequestMapping(value="/Ofertas/{id}",method = RequestMethod.PUT)        
    public ResponseEntity<?> agregarOferta(@RequestBody Oferta oferta, @PathVariable("id") int id){ 
       Publicante publi = publicru.findOne(id);
       
       if(publi == null)
           return new ResponseEntity<>("El Publicante No Existe", HttpStatus.INTERNAL_SERVER_ERROR);
       
        try {
            persistenci.addOferta(publi,oferta);
        } catch (tserviceExceptions ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
       return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
    
    @RequestMapping(value="/Ofertas/aplicarOferta/{idpostulante}/{idoferta}",method = RequestMethod.PUT)        
      public ResponseEntity<?> aplicarOferta(@PathVariable("idpostulante") int idpostulante, @PathVariable("idoferta") int idoferta){ 
     
          Oferta ofer = oferCru.findOne(idoferta);
          
          if(ofer == null)
              return new ResponseEntity<>("La Oferta no existe",HttpStatus.INTERNAL_SERVER_ERROR);
          
          Postulante post = poscru.findOne(idpostulante);
          
          if(post == null)
              return new ResponseEntity<>("El postulante no existe",HttpStatus.INTERNAL_SERVER_ERROR);
          
          
        try {
            persistenci.aplicarOferta(post,ofer);
        } catch (tserviceExceptions ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
       
        return new ResponseEntity<>(HttpStatus.ACCEPTED);

    }
  
    @RequestMapping(value="/Ofertas/agregarEmpleadoOferta/{idpostulante}/{idoferta}",method = RequestMethod.PUT)        
      public ResponseEntity<?> agregarEmpleadoOferta(@PathVariable("idpostulante") int idpostulante, @PathVariable("idoferta") int idoferta){ 
     
        Oferta ofer = oferCru.findOne(idoferta);
          
        if(ofer == null)
            return new ResponseEntity<>("La Oferta no existe",HttpStatus.INTERNAL_SERVER_ERROR);
          
        Postulante post = poscru.findOne(idpostulante);
          
        if(post == null)
            return new ResponseEntity<>("El postulante no existe",HttpStatus.INTERNAL_SERVER_ERROR);
          
        try {
            persistenci.addEmpleadoOferta(post,ofer);
        }catch (tserviceExceptions ex) {
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        return new ResponseEntity<>("Ok", HttpStatus.ACCEPTED);
       }
      
        @RequestMapping(value="/licencias",method = RequestMethod.GET)
        public List<Licencias> consultarLicencias() throws ResourceNotFoundException {
            return persistenci.traerLicencias();
        }
        
        @RequestMapping(value="/pagarlicencias/{idLicencia}/usuario/{usuario}",method = RequestMethod.POST)
        public ResponseEntity<?> pagarLicencia(@PathVariable("usuario") int usuario, @PathVariable("idLicencia") String idLicencia, @RequestBody InformacionPago pago) {
            Publicante publi = publicru.findOne(usuario);
            
            persistenci.login(publi);
            
            Licencias licencia = licenciaCrud.findOne(Integer.parseInt(idLicencia));
            try{
                persistenci.realizarPago(licencia, pago);
            }catch(Exception e){
                return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            
            return new ResponseEntity<>("Ok", HttpStatus.ACCEPTED); 
        }
    */
}