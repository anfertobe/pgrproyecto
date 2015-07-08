package com.tservice.Model;
// Generated 30-jun-2015 13:06:08 by Hibernate Tools 4.3.1


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Calificacion generated by hbm2java
 */
@Entity
@Table(name="calificacion")
public class Calificacion  implements java.io.Serializable {


     private int id;
     private Eventos eventos;
     private Noticias noticias;
     private Usuarios usuarios;
     private String tipo;
     private Integer calificacion;

    public Calificacion() {
    }

	
    public Calificacion(int id, Eventos eventos, Noticias noticias, Usuarios usuarios) {
        this.id = id;
        this.eventos = eventos;
        this.noticias = noticias;
        this.usuarios = usuarios;
    }
    public Calificacion(int id, Eventos eventos, Noticias noticias, Usuarios usuarios, String tipo, Integer calificacion) {
       this.id = id;
       this.eventos = eventos;
       this.noticias = noticias;
       this.usuarios = usuarios;
       this.tipo = tipo;
       this.calificacion = calificacion;
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
    @JoinColumn(name="eventos_id", nullable=false)
    public Eventos getEventos() {
        return this.eventos;
    }
    
    public void setEventos(Eventos eventos) {
        this.eventos = eventos;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="noticias_id", nullable=false)
    public Noticias getNoticias() {
        return this.noticias;
    }
    
    public void setNoticias(Noticias noticias) {
        this.noticias = noticias;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="usuarios_identificacion", nullable=false)
    public Usuarios getUsuarios() {
        return this.usuarios;
    }
    
    public void setUsuarios(Usuarios usuarios) {
        this.usuarios = usuarios;
    }

    
    @Column(name="tipo", length=8)
    public String getTipo() {
        return this.tipo;
    }
    
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    
    @Column(name="calificacion")
    public Integer getCalificacion() {
        return this.calificacion;
    }
    
    public void setCalificacion(Integer calificacion) {
        this.calificacion = calificacion;
    }




}


