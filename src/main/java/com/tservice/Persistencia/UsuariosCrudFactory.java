/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tservice.Persistencia;

import com.tservice.Model.*;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * @author andres
 */
public interface UsuariosCrudFactory extends CrudRepository<Usuarios, String>{
        @Query("select u.carne, u.nombre, u.email, u.perfil, u.semestre from Usuarios u where u.carne = :idcarne")
        public List<Usuarios> search(@Param("idcarne") String idcarne);
        
        @Query("select m.usuariosByUsuariodestino from Mensajes m where m.usuariosByUsuariosorigen.carne = :idcarne group by m.usuariosByUsuariodestino.carne")
        public List<Usuarios> searchContactos(@Param("idcarne") String idcarne);
        
        @Query("select new map(u.nombre as nombre, u.carne as carne, u.semestre as semestre, c.nombre as carrera ) from Usuarios u, Carreras c where u.carne like :idcarne group by u.nombre order by c.nombre, u.nombre, u.semestre desc")
        public List<Usuarios> searchContactosByCarne(@Param("idcarne") String idcarne);
        
        @Query("select new map(u.nombre as nombre, u.carne as carne, u.semestre as semestre, c.nombre as carrera ) from Usuarios u, Carreras c where u.nombre like :nombre group by u.nombre order by c.nombre, u.nombre, u.semestre desc")
        public List<Usuarios> searchContactosByNombre(@Param("nombre") String nombre);
        
        @Query("select new map(u.nombre as nombre, u.carne as carne, u.semestre as semestre, c.nombre as carrera ) from Usuarios u, Carreras c where lower(c.nombre) like :carrera group by u.nombre order by c.nombre, u.nombre, u.semestre desc")
        public List<Usuarios> searchContactosByCarrera(@Param("carrera") String carrera);
        
        @Query("select new map(u.nombre as nombre, u.carne as carne, u.semestre as semestre, c.nombre as carrera ) from Usuarios u, Carreras c where u.carne like :keySearch or lower(u.nombre) like :keySearch or lower(c.nombre) like :keySearch group by u.nombre order by c.nombre, u.nombre, u.semestre desc")
        public List<Usuarios> pruebaQuery(@Param("keySearch") String keySearch); 
}
