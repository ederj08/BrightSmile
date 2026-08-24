package com.brightsmile.backend.cita;

import com.brightsmile.backend.servicio.Servicio;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "cita")
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del paciente es obligatorio")
    @Column(name = "paciente_nombre", nullable = false)
    private String pacienteNombre;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email no tiene formato válido")
    @Column(name = "paciente_email", nullable = false)
    private String pacienteEmail;

    @NotBlank(message = "El telefono es obligatorio")
    @Column(name = "paciente_telefono", nullable = false)
    private String pacienteTelefono;

    @NotBlank(message = "El tipo de documento es obligatorio")
    @Column(name = "tipo_documento")
    private String tipoDocumento;

    @NotBlank(message = "El número de documento es obligatorio")
    @Column(name = "numero_documento")
    private String numeroDocumento;

    @NotNull(message = "La fecha es obligatoria")
    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @NotNull(message = "La hora es obligatoria")
    @Column(name = "hora", nullable = false)
    private LocalTime hora;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "servicio_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

    private Servicio servicio;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoCita estado = EstadoCita.PENDIENTE;

    @Column(name = "notas", length = 500)
    private String notas;

    public Cita() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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

    public Servicio getServicio() { return servicio; }
    public void setServicio(Servicio servicio) { this.servicio = servicio; }

    public EstadoCita getEstado() { return estado; }
    public void setEstado(EstadoCita estado) { this.estado = estado; }

    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }

    public String getTipoDocumento(){return tipoDocumento;}
    public void setTipoDocumento(String tipoDocumento) { this.tipoDocumento = tipoDocumento;}

    public String getNumeroDocumento(){return numeroDocumento;}
    public void setNumeroDocumento(String numeroDocumento) { this.numeroDocumento = numeroDocumento;}
}