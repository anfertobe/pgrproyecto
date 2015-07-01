package com.tservice.Model;
// Generated 30-jun-2015 13:06:08 by Hibernate Tools 4.3.1


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Intereses generated by hbm2java
 */
@Entity
@Table(name="intereses")
public class Intereses  implements java.io.Serializable {


     private int id;
     private Carreras carreras;
     private Eventos eventos;
     private Noticias noticias;
     private String descripcion;
     private String nombre;

    public Intereses() {
    }

	
    public Intereses(int id) {
        this.id = id;
    }
    public Intereses(int id, Carreras carreras, Eventos eventos, Noticias noticias, String descripcion, String nombre) {
       this.id = id;
       this.carreras = carreras;
       this.eventos = eventos;
       this.noticias = noticias;
       this.descripcion = descripcion;
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

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="carreras_id")
    public Carreras getCarreras() {
        return this.carreras;
    }
    
    public void setCarreras(Carreras carreras) {
        this.carreras = carreras;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="eventos_id")
    public Eventos getEventos() {
        return this.eventos;
    }
    
    public void setEventos(Eventos eventos) {
        this.eventos = eventos;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="noticias_id")
    public Noticias getNoticias() {
        return this.noticias;
    }
    
    public void setNoticias(Noticias noticias) {
        this.noticias = noticias;
    }

    
    @Column(name="descripcion")
    public String getDescripcion() {
        return this.descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    
    @Column(name="nombre", length=100)
    public String getNombre() {
        return this.nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }




}


