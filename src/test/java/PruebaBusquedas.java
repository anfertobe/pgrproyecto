
import com.tservice.BD.Connect;
import com.tservice.Model.Carreras;
import com.tservice.Model.Eventos;
import com.tservice.Model.Usuarios;
import com.tservice.Persistencia.CalificacionCrudFactory;
import com.tservice.Persistencia.CarrerasCrudFactory;
import com.tservice.Persistencia.EventosCrudFactory;
import com.tservice.Persistencia.GruposCrudFactory;
import com.tservice.Persistencia.InteresesCrudFactory;
import com.tservice.Persistencia.NoticiasCrudFactory;
import com.tservice.Persistencia.UsuariosCrudFactory;
import com.tservice.facade.App;
import com.tservice.facade.facade;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
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
   static UsuariosCrudFactory usuaCrud;
    static facade fac=new facade();
    
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
    
   public static void main (String [ ] args) {
      
      try {
          ArrayList<String> res=(ArrayList<String>) Connect.runQuery(sEventosPreferencia.replace(":valor","1"));

          System.out.println("Longitud "+res.size());
          for(String reps:res){
              System.out.println(reps);
          }
          
          res=(ArrayList<String>) Connect.runQuery(sEventosDefault.replace(":valor","1"));

          System.out.println("Longitud "+res.size());
          for(String reps:res){
              System.out.println(reps);
          }
    
           res=(ArrayList<String>) Connect.runQuery(sNoticiasPreferencia.replace(":valor","1"));

          System.out.println("Longitud "+res.size());
          for(String reps:res){
              System.out.println(reps);
          }
          
          res=(ArrayList<String>) Connect.runQuery(sNoticiasDefault.replace(":valor","1"));

          System.out.println("Longitud "+res.size());
          for(String reps:res){
              System.out.println(reps);
          }
    
          
      } catch (ClassNotFoundException ex) {
          Logger.getLogger(PruebaBusquedas.class.getName()).log(Level.SEVERE, null, ex);
      } catch (SQLException ex) {
          Logger.getLogger(PruebaBusquedas.class.getName()).log(Level.SEVERE, null, ex);
      }
   }
    
}
