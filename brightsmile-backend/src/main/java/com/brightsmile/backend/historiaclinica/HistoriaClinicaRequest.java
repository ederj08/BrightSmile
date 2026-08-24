package com.brightsmile.backend.historiaclinica;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class HistoriaClinicaRequest {

    @NotBlank
    private String numeroDocumento;

    @NotBlank
    private String pacienteNombre;

    @NotNull
    private LocalDate fecha;

    @NotBlank
    private String diagnostico;

    private String tratamientoRealizado;
    private String observaciones;
    private Long citaId;

    //Getters and Setters

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

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Long getCitaId() {
        return citaId;
    }

    public void setCitaId(Long citaId) {
        this.citaId = citaId;
    }




}
