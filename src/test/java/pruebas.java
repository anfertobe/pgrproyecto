/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.tservice.Model.Carreras;
import com.tservice.Model.Usuarios;
import com.tservice.exceptions.servergcmExceptions;
import com.tservice.facade.facade;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author andres
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContextH2.xml"})
public class pruebas {

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Autowired
    facade l;
     @Test
     public void hello() throws servergcmExceptions 
     {
        Usuarios usu = new Usuarios("2087459");
        usu.setPassword("2087459");
        usu.setEmail("andres.torres-b@mail.escuelaing.edu.co");
        usu.setIdentificaciongoogle("dmf48f39f5b4uf8d49d9484vbrudcn2unx9sn038uc932948");
        usu.setNombre("Andres Fernando Torres Beltran");
        Carreras carrera = new Carreras();
        carrera.setNombre("Ingenieria Sistemas");
        carrera.setId(2);
        l.Registro(usu, carrera);
     }
}
