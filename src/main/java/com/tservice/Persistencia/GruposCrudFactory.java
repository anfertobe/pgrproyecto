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
 *
 * @author andres
 */
public interface GruposCrudFactory extends CrudRepository<Grupos, Integer>{
        @Query("select new map(m.nombre as nombre, m.usuarioses.size as numcontactos) from Grupos m where :idcarne = some elements(m.usuarioses)")
        public List<Grupos> searchGrupos(@Param("idcarne") String idcarne);
}
