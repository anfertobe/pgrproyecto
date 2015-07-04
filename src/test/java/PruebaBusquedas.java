
import com.tservice.Model.Usuarios;
import com.tservice.Persistencia.CalificacionCrudFactory;
import com.tservice.Persistencia.CarrerasCrudFactory;
import com.tservice.Persistencia.EventosCrudFactory;
import com.tservice.Persistencia.GruposCrudFactory;
import com.tservice.Persistencia.InteresesCrudFactory;
import com.tservice.Persistencia.NoticiasCrudFactory;
import com.tservice.Persistencia.UsuariosCrudFactory;
import com.tservice.facade.App;
import org.springframework.beans.factory.annotation.Autowired;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author LuisAndres
 */
public class PruebaBusquedas {
    
   
    @Autowired
    static UsuariosCrudFactory usuCrud;
    @Autowired
    static CarrerasCrudFactory carreCrud;
    @Autowired
    static EventosCrudFactory eventosCrud;
    @Autowired
    static NoticiasCrudFactory noticiasCrud;
    @Autowired
    static CalificacionCrudFactory calificacionCrud;
    @Autowired
    static InteresesCrudFactory interesesCrud;
    @Autowired
    App envio;
    @Autowired
    static GruposCrudFactory gruposCrud;
     
    
    
   public static void main (String [ ] args) {
        
    

   }
    
}
