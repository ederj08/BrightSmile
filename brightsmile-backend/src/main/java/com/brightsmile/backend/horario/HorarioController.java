package com.brightsmile.backend.horario;


import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/horarios")
public class HorarioController {

    private  final HorarioService horarioService;

    public HorarioController(HorarioService horarioService){
        this.horarioService = horarioService;
    }

    //GET / api/v1/horarios/disponibles/{diasemana}
    //Público - el chatbot lo usa para mostrar horarios disponibles
    //Ejemplo: /horarios/disponibles/1->horarios del lunes

    @GetMapping("/disponibles/{diaSemana}")
    public ResponseEntity<List<HorarioDisponible>>getDisponibles(
            @PathVariable Integer diaSemana){
        return ResponseEntity.ok(horarioService.getHorariosDisponibles(diaSemana));
    }

    //GET /api/v1/horarios
    //Solo ADMIN - ve todos los horarios incluyendo bloqueados
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<HorarioDisponible>>getTodos(){
        return ResponseEntity.ok(horarioService.getTodos());
    }

    //GET /api/v1/horarios/dia/{diaSemana}
    //Solo ADMIN - ve todos los horarios de un dia especifico
    @GetMapping("/dia/{diaSemana}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<HorarioDisponible>> getPorDia(
            @PathVariable Integer diaSemana) {
        return ResponseEntity.ok(horarioService.getHorariosDisponibles(diaSemana));
    }
    //POST /api/v1/horarios
    //Solo ADMIN puede crear horarios
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<HorarioDisponible> crearHorario(
            @Valid @RequestBody HorarioDisponible horario){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(horarioService.crearHorario(horario));
    }

    //PUT /api/v1/horarios/{id}/toggle
    //Solo ADMIN puede bloquear o desbloquear horarios
    @PutMapping("/{id}/toggle")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<HorarioDisponible> toggleDisponibilidad(
            @PathVariable Long id) {
        return ResponseEntity.ok(horarioService.toggleDisponibilidad(id));
    }
    //DELETE /api/v1/horarios/{id}
    // Solo ADMIN puede eliminar horarios
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void>eliminarHorario(@PathVariable Long id){
        horarioService.eliminarHorario(id);
        return ResponseEntity.noContent().build();
    }
}
