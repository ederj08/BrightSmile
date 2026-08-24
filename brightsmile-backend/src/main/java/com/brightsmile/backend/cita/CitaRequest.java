package com.brightsmile.backend.cita;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

// DTO que representa los datos que llegan del frontend para crear una cita
// Separa los datos de entrada de la entidad Cita
// Así el frontend no puede modificar campos como "estado" o "id" directamente
public class CitaRequest {

    @NotBlank(message = "El nombre es obligatorio")
    private String pacienteNombre;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Email inválido")
    private String pacienteEmail;

    @NotBlank(message = "El teléfono es obligatorio")
    private String pacienteTelefono;

    @NotBlank(message = "El tipo de documento es obligatorio")
    private String tipoDocumento;

    @NotBlank(message = "El número de documento es obligatorio")
    private String numeroDocumento;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    @NotNull(message = "La hora es obligatoria")
    private LocalTime hora;

    // ID del servicio que quiere el paciente
    @NotNull(message = "El servicio es obligatorio")
    private Long servicioId;

    // Notas opcionales — el chatbot puede agregar contexto aquí
    private String notas;

    public CitaRequest() {}

    public String getPacienteNombre() { return pacienteNombre; }
    public void setPacienteNombre(String pacienteNombre) { this.pacienteNombre = pacienteNombre; }

    public String getPacienteEmail() { return pacienteEmail; }
    public void setPacienteEmail(String pacienteEmail) { this.pacienteEmail = pacienteEmail; }

    public String getPacienteTelefono() { return pacienteTelefono; }
    public void setPacienteTelefono(String pacienteTelefono) { this.pacienteTelefono = pacienteTelefono; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public LocalTime getHora() { return hora; }
    public void setHora(LocalTime hora) { this.hora = hora; }

    public Long getServicioId() { return servicioId; }
    public void setServicioId(Long servicioId) { this.servicioId = servicioId; }

    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }

    public String getTipoDocumento() {return tipoDocumento;}

    public void setTipoDocumento(String tipoDocumento) { this.tipoDocumento = tipoDocumento;}

    public String getNumeroDocumento () {return  numeroDocumento;}

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;}
}
