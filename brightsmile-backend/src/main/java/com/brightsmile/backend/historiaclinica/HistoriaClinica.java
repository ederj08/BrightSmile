package com.brightsmile.backend.historiaclinica;

import com.brightsmile.backend.cita.Cita;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Entity
@Table(name = "historia_clinica")

public class HistoriaClinica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El numero de documento es obligatorio")
    @Column(name = "numero_documento", nullable = false)
    private String numeroDocumento;

    @NotBlank(message = "El nombre del paciente es obligatorio")
    @Column(name = "paciente_nombre", nullable = false)
    private String pacienteNombre;

    @NotNull(message = "La fecha es obligatoria")
    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @NotBlank(message = "El diagnostico es obligatorio")
    @Column(name = "diagnostico", length = 1000, nullable = false)
    private String diagnostico;

    @Column (name = "tratamiento_realizado", length = 1000)
    private String tratamientoRealizado;

    @Column(name = "observaciones", length = 1000)
    private String observaciones;

    //Vínculo opcional a la cita que originó esta entrada - puede ser null
    //si el dentista agrega una nota sin estar ligada a una cita puntual

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cita_id")
    @JsonIgnore
    private Cita cita;

    @Column(name = "creado_por")
    private String creadoPor; //username del admin/dentista

    public HistoriaClinica(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String getPacienteNombre() {
        return pacienteNombre;
    }

    public void setPacienteNombre(String pacienteNombre) {
        this.pacienteNombre = pacienteNombre;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public String getTratamientoRealizado() {
        return tratamientoRealizado;
    }

    public void setTratamientoRealizado(String tratamientoRealizado) {
        this.tratamientoRealizado = tratamientoRealizado;
    }

    public Cita getCita() {
        return cita;
    }

    public void setCita(Cita cita) {
        this.cita = cita;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getCreadoPor() {
        return creadoPor;
    }

    public void setCreadoPor(String creadoPor) {
        this.creadoPor = creadoPor;
    }
}
    