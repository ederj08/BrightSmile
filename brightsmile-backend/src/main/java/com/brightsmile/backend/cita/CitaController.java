package com.brightsmile.backend.cita;

import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/citas")
public class CitaController {

    private final CitaService citaService;

    public CitaController(CitaService citaService) {
        this.citaService = citaService;
    }

    // GET /api/v1/citas
    // Solo ADMIN y RECEP pueden ver todas las citas
    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_RECEP')")
    public ResponseEntity<List<Cita>> todasLasCitas() {
        return ResponseEntity.ok(citaService.todasLasCitas());
    }

    // GET /api/v1/citas/fecha?fecha=2026-06-15
    // Solo ADMIN y RECEP - citas de un día especifico
    @GetMapping("/fecha")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_RECEP')")
    public ResponseEntity<List<Cita>> citasPorFecha(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return ResponseEntity.ok(citaService.citasPorFecha(fecha));
    }

    // GET /api/v1/citas/disponibilidad?fecha=2026-06-23&hora=09:00
    // Público
    @GetMapping("/disponibilidad")
    public ResponseEntity<Boolean> verificarDisponibilidad(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime hora) {
        return ResponseEntity.ok(citaService.estaDisponible(fecha, hora));
    }

    //Público - el paciente busca sus propias citass para poder cancelarlas.
    @GetMapping("/buscar")
    public ResponseEntity<List<Cita>>buscarPorDocumento(
            @RequestParam String numeroDocumento){
        return ResponseEntity.ok(citaService.citasPorDocumento(numeroDocumento));
    }

    // GET /api/v1/citas/paciente/{numeroDocumento}
    // Solo ADMIN y RECEP — buscar todas las citas de un paciente
    @GetMapping("/paciente/{numeroDocumento}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_RECEP')")
    public ResponseEntity<List<Cita>> citasPorDocumento(
            @PathVariable String numeroDocumento) {
        return ResponseEntity.ok(citaService.citasPorDocumento(numeroDocumento));
    }

    // POST /api/v1/citas
    // Público - el paciente no necesita token para crear una cita
    @PostMapping
    public ResponseEntity<Cita> crearCita(@Valid @RequestBody CitaRequest request) {
        Cita nueva = citaService.crearCita(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
    }

    // PUT /api/v1/citas/{id}/estado
    // Solo ADMIN y RECEP pueden cambiar el estado
    @PutMapping("/{id}/estado")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_RECEP')")
    public ResponseEntity<Cita> cambiarEstado(
            @PathVariable Long id,
            @RequestParam EstadoCita estado) {
        return ResponseEntity.ok(citaService.cambiarEstado(id, estado));
    }

    // PUT /api/v1/citas/{id}/cancelar
    // Público - el paciente cancela su cita verificando su número de documento
    @PutMapping("/{id}/cancelar")
    public ResponseEntity<Cita> cancelarCitaPaciente(
            @PathVariable Long id,
            @RequestParam String numeroDocumento) {
        Cita cita = citaService.getCita(id);
        if (!cita.getNumeroDocumento().equals(numeroDocumento)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(citaService.cancelarCita(id));
    }

    // PUT /api/v1/citas/completar-pasadas
    // Solo ADMIN — marca como COMPLETADA todas las citas PENDIENTE con fecha anterior a hoy
    @PutMapping("/completar-pasadas")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Integer> completarCitasPasadas() {
        int actualizadas = citaService.completarCitasPasadas();
        return ResponseEntity.ok(actualizadas);
    }
    //DELETE /api/v1/citas/{id}
    //solo ADMIN - elimina citas CANCELADAS o COMPLETADAS
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity <Void> eliminarCita(@PathVariable Long id){
        citaService.eliminarCita(id);
        return ResponseEntity.noContent().build();
    }
}