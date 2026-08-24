package com.brightsmile.backend.historiaclinica;


import com.brightsmile.backend.cita.Cita;
import com.brightsmile.backend.cita.CitaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class HistoriaClinicaService {

    private final HistoriaClinicaRepository repository;
    private final CitaRepository citaRepository;

    public HistoriaClinicaService(HistoriaClinicaRepository repository, CitaRepository citaRepository) {
        this.repository = repository;
        this.citaRepository = citaRepository;
    }
    public HistoriaClinica crear(HistoriaClinicaRequest request, String usuarioActual){

        HistoriaClinica entrada = new HistoriaClinica();
        entrada.setNumeroDocumento(request.getNumeroDocumento());
        entrada.setPacienteNombre(request.getPacienteNombre());
        entrada.setFecha(request.getFecha());
        entrada.setDiagnostico(request.getDiagnostico());
        entrada.setTratamientoRealizado(request.getTratamientoRealizado());
        entrada.setObservaciones(request.getObservaciones());
        entrada.setCreadoPor(usuarioActual);

        if (request.getCitaId()!=null){
            Cita cita = citaRepository.findById(request.getCitaId())
                    .orElseThrow(()->new NoSuchElementException("Cita no encontrada"));
            entrada.setCita(cita);
        }
        return repository.save(entrada);
    }
    public List<HistoriaClinica>buscarPorDocumento(String numeroDocumento){
        return repository.findByNumeroDocumentoOrderByFechaDesc(numeroDocumento);
    }
    public HistoriaClinica obtnerPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(()->new NoSuchElementException("Entrada de historia clinica no encontrada"));
    }

}
