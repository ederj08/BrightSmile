package com.brightsmile.backend.historiaclinica;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;


public interface HistoriaClinicaRepository extends JpaRepository<HistoriaClinica,Long>{
   List<HistoriaClinica> findByNumeroDocumentoOrderByFechaDesc(String numeroDocumento);

   @Modifying
   @Query("UPDATE HistoriaClinica h SET h.cita = null WHERE h.cita.id = :citaId")
   void desvincularCita(@Param("citaId")Long citaId);
}
