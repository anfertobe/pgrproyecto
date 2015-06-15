/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tservice.Persistencia;

import com.tservice.Model.*;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author andres
 */
public interface EventosCrudFactory extends CrudRepository<Eventos, Integer>{
    
}
