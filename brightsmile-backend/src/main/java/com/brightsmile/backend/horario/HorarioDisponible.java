package com.brightsmile.backend.horario;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.autoconfigure.web.WebProperties;

//Representa un bloque de tiempo disponible en la clínica
//Ejemplo: Lunes de 9:00 AM - Disponible.

@Entity
@Table  (name = "horario_disponible")

public class HorarioDisponible {

@Id
@GeneratedValue (strategy = GenerationType.IDENTITY)
private Long id;

//Día de la semana: 1=lunes,2=martes ....
@NotNull
@Column (name = "dia_semana",nullable = false)
private Integer diaSemana;

//Hora de inicio en formato "HH:mm" ejemplo "09:00"
@NotNull
@Column (name = "hora_inicio",nullable = false)
private String horaInicio;

//Hora de fin en formato "HH:mm" ejemplo "0+:30"
@NotNull
@Column (name = "hora_fin", nullable = false)
private String horaFin;

//si este horario esta disponible o bloqueado por el admin
@Column (name = "disponible", nullable = false)
private boolean disponible = true;

public HorarioDisponible (){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(Integer diaSemana) {
        this.diaSemana = diaSemana;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }
}
