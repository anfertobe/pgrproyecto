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
public interface CarrerasCrudFactory extends CrudRepository<Carreras, Integer>{
    
    @Query("select id, nombre from Carreras")
        public List<Carreras> search();
        
    @Query("select new map(id as id, nombre as nombre) from Carreras")
    public List<Carreras> searchcarreras();
    
    @Query("from Carreras c where lower(c.nombre) Like :carrera")
    public List<Carreras> searchcarrerasByKey(@Param("carrera") String carrera);
}
