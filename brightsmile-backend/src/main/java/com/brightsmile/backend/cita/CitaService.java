package com.brightsmile.backend.cita;

import com.brightsmile.backend.historiaclinica.HistoriaClinicaRepository;
import com.brightsmile.backend.servicio.Servicio;
import com.brightsmile.backend.servicio.ServicioService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CitaService {

    private final CitaRepository citaRepository;
    private final ServicioService servicioService;
    private final HistoriaClinicaRepository historiaClinicaRepository;

    //Inyecta CitaRepository y ServicioService
    //Necesita ServicioService para verificar que el servicio existe al crear cita

    public CitaService(CitaRepository citaRepository,
                       ServicioService servicioService,
                       HistoriaClinicaRepository historiaClinicaRepository) {
        this.citaRepository = citaRepository;
        this.servicioService = servicioService;
        this.historiaClinicaRepository=historiaClinicaRepository;
    }

    //GET todas las citas - para el panel admin
    @Transactional(readOnly = true)
    public List<Cita> todasLasCitas() {
        return citaRepository.findAll();
    }

    //GET citas de una fecha especifica
    @Transactional(readOnly = true)
    public List<Cita> citasPorFecha(LocalDate fecha) {
        return citaRepository.findByFecha(fecha);
    }

    //GET citas por estado - Example: todas las PENDIENTES
    @Transactional(readOnly = true)
    public List<Cita> citasPorEstado(EstadoCita estado) {
        return citaRepository.findByEstado(estado);
    }

    //GET citas de un paciente por email
    @Transactional(readOnly = true)
    public List<Cita> citasPorPaciente(String email) {
        return citaRepository.findByPacienteEmail(email);
    }

    //GET citas en un rango por fechas
    @Transactional(readOnly = true)
    public List<Cita> citasEntreFechas(LocalDate inicio, LocalDate fin) {
        return citaRepository.findByFechaBetween(inicio, fin);
    }

    //GET una cita por ID
    @Transactional(readOnly = true)
    public Cita getCita(Long id) {
        return citaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Cita con id" + id + "no existe"));
    }

    //POST crear una cita - la más importante
    @Transactional
    public Cita crearCita(CitaRequest request) {

        //No se puede egendar en el pasado
        if (request.getFecha().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("No se puede agendar una cita en el pasado");
        }
        //No se puede agendar agendar en el dia domingo (dia 7)
        if (request.getFecha().getDayOfWeek().getValue() == 7) {
            throw new IllegalArgumentException("La clinica no atiende los domingos");
        }
        //Verificar que el servicio existe y está activo
        Servicio servicio = servicioService.getServicio(request.getServicioId());
        if (!servicio.isActivo()) {
            throw new IllegalArgumentException("El servicio seleccionado no está disponible");
        }
        //verifica que no haya otra cita en esa fecha y hora
        if (citaRepository.existsByFechaAndHora(request.getFecha(), request.getHora())) {
            throw new IllegalArgumentException("Ya existe una cita agendada para esa fecha y hora ");
        }
        //Si paso todas las validaciones, crea la cita
        Cita cita = new Cita();
        cita.setPacienteNombre(request.getPacienteNombre());
        cita.setPacienteEmail(request.getPacienteEmail());
        cita.setPacienteTelefono(request.getPacienteTelefono());
        cita.setFecha(request.getFecha());
        cita.setHora(request.getHora());
        cita.setServicio(servicio);
        cita.setNotas(request.getNotas());
        cita.setTipoDocumento(request.getTipoDocumento());
        cita.setNumeroDocumento(request.getNumeroDocumento());
        cita.setEstado(EstadoCita.PENDIENTE);

        return citaRepository.save(cita);
    }

    //PUT cambiar estado de una cita
    @Transactional
    public Cita cambiarEstado(Long id, EstadoCita nuevoEstado) {
        Cita cita = getCita(id);

        //No se puede cambiar el estado de una cita cancelada
        if (cita.getEstado() == EstadoCita.CANCELADA) {
            throw new IllegalArgumentException("No se puede modificar una cita cancelada");
        }
        //No se puede cambiar el estado de una cita completada
        if (cita.getEstado() == EstadoCita.COMPLETADA) {
            throw new IllegalArgumentException("No se peude modificar una cita completada ");
        }
        cita.setEstado(nuevoEstado);
        return citaRepository.save(cita);
    }

    //DELETE cancelar cita
    @Transactional
    public Cita cancelarCita(Long id) {
        return cambiarEstado(id, EstadoCita.CANCELADA);
    }

    //Verifica si una fecha y hora están disponibles
    //Lo usa el chatbot para responder disponibilidad
    @Transactional(readOnly = true)
    public boolean estaDisponible(LocalDate fecha, LocalTime hora) {
        return !citaRepository.existsByFechaAndHora(fecha, hora);
    }

    //Marca como COMPLETADA todas las citas PENDIENTES con fecha anterior a hoy

    @Transactional
    public int completarCitasPasadas() {
        List<Cita> pasadas = citaRepository.findByEstadoAndFechaBefore(
                EstadoCita.PENDIENTE, LocalDate.now()
        );
        pasadas.forEach(c -> c.setEstado(EstadoCita.COMPLETADA));
        citaRepository.saveAll(pasadas);
        return pasadas.size();
    }

    @Transactional(readOnly = true)
    public List<Cita> citasPorDocumento(String numeroDocumento) {
        return citaRepository.findByNumeroDocumento(numeroDocumento);
    }

    @Transactional
    public void eliminarCita(Long id) {
        Cita cita = getCita(id);
        if (cita.getEstado() == EstadoCita.PENDIENTE) {
            throw new IllegalArgumentException(
                    "No se puede eliminar una cita pendiente, cancélala primero"
            );
        }
        historiaClinicaRepository.desvincularCita(id);
        citaRepository.deleteById(id);
    }
}
