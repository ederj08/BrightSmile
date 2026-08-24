package com.brightsmile.backend.servicio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface ServicioRepository extends JpaRepository<Servicio,Long>{

    //Trae solo los servicios activos - los que se pueden agendar
    List<Servicio> findByActivoTrue();

    //Verifica si ya existe un servicio con ese nombre
    boolean existsByNombre(String nombre);
}


