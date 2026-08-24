package com.brightsmile.backend.servicio;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

//@Entity le dice a Hibernate que esta es una tabla de Bd
//@Table define el nombre exacto de la tabla en PostgreSQL

@Entity
@Table (name ="Servicio")

public class Servicio {

    //@Id marca este campo como clave primaria
    //@GenerateValue con IDENTITY hace que PostgreSQL genere el Id automáticamente
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

// @NotBlank valida que el campo no sea null ni vacío
//@Column define el nombre de la columna en ala tabla

@NotBlank(message = "El nombre del servicio es obligatorio ")
@Column(name = "nombre",nullable = false)
private String nombre;

//Descripción opcional del servicio
    @Column( name= "descripcion")
    private  String descripcion;

//Duracion del servicio en minutos - necesario para calcular disponibilidad
//@Positive valida que el número sea mayor a 0

@NotNull(message = "La duración es obligatoria")
@Positive(message = "La duración debe ser mayor a 0")
@Column(name = "duracion_minutos", nullable = false)
private Integer duracionMinutos;



//Si el servicio está activo o fue desactivado
//true = disponibilidad para agendar, false = disponible
@Column(name = "activo",nullable = false)
private boolean activo = true;

//Constructor vacío obligatorio para JPA
public Servicio(){}

//Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }


    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}

