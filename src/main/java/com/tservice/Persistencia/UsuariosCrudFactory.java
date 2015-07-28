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
}
