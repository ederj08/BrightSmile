package com.brightsmile.backend.cita;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface CitaRepository extends JpaRepository <Cita, Long> {

    //Trae todas las citas de una fecha especifica
    List<Cita> findByFecha(LocalDate fecha);

    //Verifica si ya existe una cita en esa fecha y hora - para evitar doble reserva
    boolean existsByFechaAndHora(LocalDate fecha, LocalTime hora);

    //Trae citas por estado - ejemplo: todas las PENDIENTES
    List<Cita> findByEstado(EstadoCita estado);

    //Trae citas de un paciente por su email
    List<Cita>findByPacienteEmail(String email);

    //Trae citas de un rango de fechas - útil para el panel admin
    List<Cita> findByFechaBetween(LocalDate inicio, LocalDate fin);

    //Trae citas PENDIENTE con fecha anterior a hoy - para auto-completar
    List<Cita> findByEstadoAndFechaBefore(EstadoCita estado, LocalDate fecha);

    //Para buscar paciente por número documento
    List<Cita>findByNumeroDocumento(String numeroDocumento);

}
