package com.brightsmile.backend.historiaclinica;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/historia-clinica")
@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_DENTISTA')")
public class HistoriaClinicaController {

    private final HistoriaClinicaService service;
    private final PdfService pdfService;

    public HistoriaClinicaController(HistoriaClinicaService service, PdfService pdfService) {
        this.service = service;
        this.pdfService = pdfService;
    }

    // POST /api/v1/historia-clinica
    @PostMapping
    public ResponseEntity<HistoriaClinica> crear(
            @Valid @RequestBody HistoriaClinicaRequest request,
            Authentication authentication) {
        HistoriaClinica creada = service.crear(request, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    // GET /api/v1/historia-clinica/paciente/{numeroDocumento}
    @GetMapping("/paciente/{numeroDocumento}")
    public ResponseEntity<List<HistoriaClinica>> buscarPorDocumento(
            @PathVariable String numeroDocumento) {
        return ResponseEntity.ok(service.buscarPorDocumento(numeroDocumento));
    }

    // GET /api/v1/historia-clinica/paciente/{numeroDocumento}/pdf
    @GetMapping("/paciente/{numeroDocumento}/pdf")
    public ResponseEntity<byte[]> descargarPdf(@PathVariable String numeroDocumento) {
        List<HistoriaClinica> entradas = service.buscarPorDocumento(numeroDocumento);

        String pacienteNombre = entradas.isEmpty()
                ? "Unknown patient"
                : entradas.get(0).getPacienteNombre();

        byte[] pdf = pdfService.generarHistorialPdf(numeroDocumento, pacienteNombre, entradas);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment",
                "historia-clinica-" + numeroDocumento + ".pdf");

        return ResponseEntity.ok().headers(headers).body(pdf);
    }
}