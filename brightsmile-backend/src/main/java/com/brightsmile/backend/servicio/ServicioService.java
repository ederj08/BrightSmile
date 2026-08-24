package com.brightsmile.backend.servicio;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

//@Service le dice a spring que esta clase contiene lógica de negocio
//Spring la registra como bean y puede inyectarla en otros lugares

@Service
public class ServicioService {

    //El repository es el único camino para hablar en la BD
    //final = una vez asigando en el contructor no puede cambiar
    private final ServicioRepository servicioRepository;

    //Inyección por contructor - Spring inyecta el repository automáticamente
    public ServicioService(ServicioRepository servicioRepository){
        this.servicioRepository=servicioRepository;
    }

    //Get todos los servicios activos
    //readOnly = true -> le dice a la BD que esta operación no modifica datos
    //Esto mejora el rendimiento porque la BD no necesita prepararse para rollback
    @Transactional(readOnly = true)
    public List<Servicio>getServiciosActivos(){
        return servicioRepository.findByActivoTrue();
    }

    //GET todos los servicios incluyendo los inactivos - solo para el admin
    @Transactional (readOnly = true)
    public List<Servicio>getTodosServicios(){
        return servicioRepository.findAll();
    }

    //GET un servicio por ID
    @Transactional (readOnly = true)
    public Servicio getServicio(Long id){
        //orElsethrow-> si no encuentra el servicio lanza excepción
        //El GlobalExceptionHandler la captura y devuelve un 404 automáticamente
        return  servicioRepository.findById(id)
                .orElseThrow(()-> new NoSuchElementException("Servicio con id"+ id + "no existe"));
    }

    //POST crear servicio
    @Transactional
    public Servicio crearServicio(Servicio servicio){
        //validación - no peude haber dos servicios con el mismo nombre
        if (servicioRepository.existsByNombre(servicio.getNombre())){
            throw  new IllegalArgumentException("ya existe un servicio con el nombre: " + servicio.getNombre());
        }
        //save() hace INSERT si el objeto es nuevo, UPDATE si ya existe
        return servicioRepository.save(servicio);
    }

    //PUT actualizar servicio
    @Transactional
    public Servicio actualizarServicio(Long id, Servicio datos){
        // verifica primero que exista
        Servicio existente= getServicio(id);

        // Actualiza solo los campos que llegaron
        existente.setNombre(datos.getNombre());
        existente.setDescripcion(datos.getDescripcion());
        existente.setActivo(datos.isActivo());

        return servicioRepository.save(existente);
    }

    // DELETE lógico — no borra de la BD, solo desactiva
    // Esto es mejor práctica que borrar definitivamente
    // Si se borrara, las citas históricas perderían la referencia al servicio
    @Transactional
    public void desactivarServicio(Long id) {
        Servicio servicio = getServicio(id);
        servicio.setActivo(false);
        servicioRepository.save(servicio);
    }
}





