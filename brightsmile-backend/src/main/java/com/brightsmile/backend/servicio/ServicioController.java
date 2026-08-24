package com.brightsmile.backend.servicio;


import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@Rescontroller->esta clase recibe peticiones HTTP y devuelve JSON
//@RequestMapping-> todas las rutas inician con /api/v1/servicios

@RestController
@RequestMapping("/api/v1/servicios")
public class ServicioController {

    private final ServicioService servicioService;

    public ServicioController(ServicioService servicioService){
        this.servicioService = servicioService;
    }

    //GET /api/v1/servicios/activos
    //Público - el chatbot y el formulario del paciente lo usan sin token
    //Devuelve solo los servicios disponibles para agendar
    @GetMapping ("/activos")
    public ResponseEntity<List<Servicio>>getServiciosActivos(){
        return ResponseEntity.ok(servicioService.getServiciosActivos());
    }

    //GET /api/v1/servicios
    //solo ADMIN - ve todos los desactivados
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<Servicio>>getTodos(){
        return ResponseEntity.ok(servicioService.getTodosServicios());
    }

    //GET / api/v1/servicios/{id}
    //Autenticado - cualquier usuario logueado puede ver un servicio
    @GetMapping("/{id}")
    public ResponseEntity<Servicio> getServicio(@PathVariable Long id){
        return ResponseEntity.ok(servicioService.getServicio(id));
    }
    //POST /api/v1/servicios
    //Solo ADMIN puede crear servicios
    //@Valid activa las validaciones de @NotBLank, positive, etc.
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Servicio> crearServicio(@Valid @RequestBody Servicio servicio){
        Servicio creado = servicioService.crearServicio(servicio);
        //201 Created es más correcto que 200 ok para creaciones
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }
    //PUT /api/v1/servicios/{id}
    //Solo ADMIN puede actualizar servicios
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Servicio>actualizarServicio(
            @PathVariable Long id,
            @Valid @RequestBody Servicio servicio){
        return ResponseEntity.ok(servicioService.actualizarServicio(id, servicio));
    }

    //DELETE /api/v1/servicio/{id}
    //Solo ADMIN puede desactivar servicios
    //No borra - hace delete lógico (actibo = false)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void>desactivarServicio(@PathVariable Long id){
        servicioService.desactivarServicio(id);
        //204 No Content -> operación exitosa sin cuerpo de respuesta
        return ResponseEntity.noContent().build();
    }
}
