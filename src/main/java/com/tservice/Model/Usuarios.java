package com.tservice.Model;
// Generated 14-jun-2015 22:57:13 by Hibernate Tools 4.3.1


import java.util.*;
import javax.persistence.*;

/**
 * Usuarios generated by hbm2java
 */
@Entity
@Table(name="usuarios"
    , uniqueConstraints = {@UniqueConstraint(columnNames="email"), @UniqueConstraint(columnNames="identificaciongoogle")} 
)
public class Usuarios  implements java.io.Serializable {


     private String carne;
     private String nombre;
     private String identificaciongoogle;
     private String email;
     private String contraseña;
     private String perfil;
     private Set<Carreras> carrerases = new HashSet(0);
     private Set<Grupos> gruposes_1 = new HashSet(0);

    public Usuarios() {
    }

    public Usuarios(String carne) {
        this.carne = carne;
    }
    
    public Usuarios(String carne, String nombre, String identificaciongoogle, String email, String contraseña) {
        this.carne = carne;
        this.nombre = nombre;
        this.identificaciongoogle = identificaciongoogle;
        this.email = email;
        this.contraseña = contraseña;
    }
	
    public Usuarios(String carne, String nombre, String identificaciongoogle, String email, String contraseña, String perfil) {
        this.carne = carne;
        this.nombre = nombre;
        this.perfil = perfil;
        this.identificaciongoogle = identificaciongoogle;
        this.email = email;
        this.contraseña = contraseña;
    }
    public Usuarios(String carne, String nombre, String identificaciongoogle, String email, String contraseña, String perfil, Set<Carreras> carrerases, Set<Grupos> gruposes_1) {
       this.carne = carne;
       this.nombre = nombre;
       this.identificaciongoogle = identificaciongoogle;
       this.email = email;
       this.contraseña = contraseña;
       this.perfil = perfil;
       this.carrerases = carrerases;
       this.gruposes_1 = gruposes_1;
    }
   
     @Id 

    
    @Column(name="carne", unique=true, nullable=false , length=20)
    public String getCarne() {
        return this.carne;
    }
    
    public void setCarne(String carne) {
        this.carne = carne;
    }

    
    @Column(name="nombre", length=100)
    public String getNombre() {
        return this.nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    
    @Column(name="identificaciongoogle", unique=true)
    public String getIdentificaciongoogle() {
        return this.identificaciongoogle;
    }
    
    public void setIdentificaciongoogle(String identificaciongoogle) {
        this.identificaciongoogle = identificaciongoogle;
    }

        @Column(name="perfil", length=45)
    public String getPerfil() {
        return this.perfil;
    }
    
    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }
    
    @Column(name="email", unique=true, length=120)
    public String getEmail() {
        return this.email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }

    
    @Column(name="contraseña", length=20)
    public String getContraseña() {
        return this.contraseña;
    }
    
    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

@ManyToMany(fetch=FetchType.LAZY)
    @JoinTable(name="carreras_has_usuarios", catalog="coswg2", joinColumns = { 
        @JoinColumn(name="usuarios_carne", nullable=false, updatable=false) }, inverseJoinColumns = { 
        @JoinColumn(name="carreras_id", nullable=false, updatable=false) })
    public Set<Carreras> getCarrerases() {
        return this.carrerases;
    }
    
    public void setCarrerases(Set<Carreras> carrerases) {
        this.carrerases = carrerases;
    }

@ManyToMany(fetch=FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(name="usuarios_has_grupos", catalog="coswg2", joinColumns = { 
        @JoinColumn(name="usuarios_carne", nullable=false, updatable=true) }, inverseJoinColumns = { 
        @JoinColumn(name="grupos_id", nullable=false, updatable=true) })
    public Set<Grupos> getGruposes_1() {
        return this.gruposes_1;
    }
    
    public void setGruposes_1(Set<Grupos> gruposes_1) {
        this.gruposes_1 = gruposes_1;
    }
}