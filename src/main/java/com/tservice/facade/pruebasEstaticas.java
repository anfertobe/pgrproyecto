/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tservice.facade;

import com.tservice.Model.Carreras;
import com.tservice.Model.Grupos;
import com.tservice.Model.Usuarios;
import com.tservice.exceptions.servergcmExceptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

/**
 *
 * @author andres
 */
public class pruebasEstaticas {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws servergcmExceptions {
        // TODO code application logic here
            ApplicationContext ap = new ClassPathXmlApplicationContext("/applicationContext.xml");
            facade l = (facade) ap.getBean(facade.class);
        
            pruebasEstaticas pruebas = new pruebasEstaticas();
            pruebas.pruebaRegistro(l);
            pruebas.pruebaLogin(l);
            pruebas.pruebaAdicionarAmigo(l);
            
    }
    
    public void pruebaRegistro(facade l) throws servergcmExceptions{
        Usuarios usu = new Usuarios("2087458");
        usu.setPassword("2087458");
        usu.setEmail("andres1.torres-b@mail.escuelaing.edu.co");
        usu.setIdentificaciongoogle("dmf48f39f5b4uf8d49d9484vbrhvhsvjxwscbn2unx9sn038uc932948");
        usu.setNombre("Andres Fernando Torres Beltran");
        Carreras carrera = new Carreras();
        carrera.setNombre("Ingenieria Sistemas");
        carrera.setId(2);
        l.Registro(usu, carrera);
    }
    
    public void pruebaLogin(facade l) throws servergcmExceptions{
        l.login("2087458", "2087458");
    }
    
    
    public void pruebaAdicionarAmigo(facade l) throws servergcmExceptions{
        Grupos grupo = new Grupos();
        grupo.setId(10);
        
        Usuarios usu = new Usuarios("2087459");
        usu.setPassword("2087459");
        usu.setEmail("andres.torres-b@mail.escuelaing.edu.co");
        usu.setIdentificaciongoogle("dmf48f39f5b4uf8d49d9484vbrhvhsvjxwssdflcbn2unx9sn038uc932948");
        usu.setNombre("Andres Fernando Torres Beltran");
        Carreras carrera = new Carreras();
        carrera.setNombre("Ingenieria Sistemas");
        carrera.setId(2);
        l.Registro(usu, carrera);
        
        l.AdiciaonarUsuariosGrupo(grupo, usu);
        
        
    }
    
}
