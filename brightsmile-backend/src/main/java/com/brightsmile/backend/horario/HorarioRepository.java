package com.brightsmile.backend.horario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HorarioRepository extends JpaRepository<HorarioDisponible, Long>{

    //Trae todos los horarios de un día especifico
    //Ejemplo: findByDiaSemana (1)-> todos los horarios del lunes

    List<HorarioDisponible> findByDiaSemana(Integer diaSemana);

    //Trae solo los horarios disponibles de un día
    List<HorarioDisponible>findByDiaSemanaAndDisponibleTrue(Integer diaSemana);
}
