package com.brightsmile.backend.horario;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class HorarioService {

    private final HorarioRepository horarioRepository;

    public HorarioService(HorarioRepository horarioRepository){
        this.horarioRepository=horarioRepository;
    }

    //GET horarios disponibles de un día especifico
    //diaSemana: 1= lunes, 2=martes,...

    @Transactional(readOnly = true)
    public List<HorarioDisponible>getHorariosDisponibles(Integer diaSemana){
        return horarioRepository.findByDiaSemanaAndDisponibleTrue(diaSemana);
    }

    //GET todos los horarios de un día - incluyendo bloqueados - para el admin
    @Transactional(readOnly = true)
    public List<HorarioDisponible>getTodosHorarios(Integer diaSemana){
        return  horarioRepository.findByDiaSemana(diaSemana);
    }

    //GET todos los horarios sin filtro
    @Transactional(readOnly = true)
    public List<HorarioDisponible> getTodos() {
        List<HorarioDisponible> lista = horarioRepository.findAll();
        return lista;
    }

    //POST crear un nuevo horario disponible
    @Transactional
    public HorarioDisponible crearHorario(HorarioDisponible horario){
        //validación: la hora de fin debe ser después de la hora de inicio
        if (horario.getHoraInicio().compareTo(horario.getHoraFin())>=0){
            throw new IllegalArgumentException("La hora de inicio debe ser anterior a la hora de fin");
        }
        return horarioRepository.save(horario);
    }
    //PUT bloquear o desbloquear un horario
    //el admin puede bloquear horarios para vacaciones, emergencias, etc
    @Transactional
    public HorarioDisponible toggleDisponibilidad(Long id){
        HorarioDisponible horario = horarioRepository.findById(id)
                .orElseThrow(()->new NoSuchElementException("Horario con Id"+id +"no existe"));

        //toggle-> si hay disponiblidad lo bloquea, si está bloqueado lo desbloquea

        horario.setDisponible(!horario.isDisponible());
        return horarioRepository.save(horario);
    }
    //DELETE eliminar horario
    @Transactional
    public void eliminarHorario(Long id){
        if (!horarioRepository.existsById(id)){
            throw new NoSuchElementException("Horario con id" + id + "no existe");
        }
        horarioRepository.deleteById(id);
    }


}
