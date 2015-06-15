package com.tservice.Model;
// Generated 14-jun-2015 22:57:13 by Hibernate Tools 4.3.1


import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Edificio generated by hbm2java
 */
@Entity
@Table(name="Edificio"
    ,catalog="coswg2"
)
public class Edificio  implements java.io.Serializable {


     private int id;
     private String nombre;

    public Edificio() {
    }

	
    public Edificio(int id) {
        this.id = id;
    }
    public Edificio(int id, String nombre) {
       this.id = id;
       this.nombre = nombre;
    }
   
     @Id 

    
    @Column(name="id", unique=true, nullable=false)
    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }

    
    @Column(name="nombre", length=45)
    public String getNombre() {
        return this.nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

}


