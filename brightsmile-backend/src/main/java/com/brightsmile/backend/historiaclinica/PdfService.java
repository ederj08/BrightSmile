package com.brightsmile.backend.historiaclinica;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.draw.LineSeparator;
import org.springframework.stereotype.Service;


import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Service
public class PdfService {

    private static final Font TITULO_FONT = new Font(Font.HELVETICA, 20, Font.BOLD, Color.decode("#0d6efd"));
    private static final Font SUBTITULO_FONT = new Font(Font.HELVETICA, 11, Font.NORMAL, Color.GRAY);
    private static final Font SECCION_FONT = new Font(Font.HELVETICA, 13, Font.BOLD);
    private static final Font ETIQUETA_FONT = new Font(Font.HELVETICA, 10, Font.BOLD);
    private static final Font TEXTO_FONT = new Font(Font.HELVETICA, 10, Font.NORMAL);
    private static final DateTimeFormatter FECHA_FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public byte[] generarHistorialPdf(String numeroDocumento, String pacienteNombre, List<HistoriaClinica> entradas) {
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Encabezado — nombre de la clínica (logo se agregará más adelante)
            Paragraph clinica = new Paragraph("✦ BrightSmile Dental", TITULO_FONT);
            clinica.setAlignment(Element.ALIGN_CENTER);
            document.add(clinica);

            Paragraph subtitulo = new Paragraph("Patient Medical History", SUBTITULO_FONT);
            subtitulo.setAlignment(Element.ALIGN_CENTER);
            subtitulo.setSpacingAfter(20);
            document.add(subtitulo);

            document.add(new LineSeparator());
            document.add(Chunk.NEWLINE);

            // Datos básicos del paciente
            Paragraph datosTitulo = new Paragraph("Patient Information", SECCION_FONT);
            datosTitulo.setSpacingAfter(8);
            document.add(datosTitulo);

            document.add(crearLineaDato("Name:", pacienteNombre));
            document.add(crearLineaDato("Document number:", numeroDocumento));
            document.add(Chunk.NEWLINE);

            document.add(new LineSeparator());
            document.add(Chunk.NEWLINE);

            // Historial de entradas
            Paragraph historialTitulo = new Paragraph("Clinical Records (" + entradas.size() + ")", SECCION_FONT);
            historialTitulo.setSpacingAfter(12);
            document.add(historialTitulo);

            if (entradas.isEmpty()) {
                document.add(new Paragraph("No clinical records found.", TEXTO_FONT));
            } else {
                for (HistoriaClinica entrada : entradas) {
                    document.add(crearBloqueEntrada(entrada));
                    document.add(Chunk.NEWLINE);
                }
            }

            document.close();
        } catch (DocumentException e) {
            throw new RuntimeException("Error al generar el PDF", e);
        }

        return out.toByteArray();
    }

    private Paragraph crearLineaDato(String etiqueta, String valor) {
        Paragraph p = new Paragraph();
        p.add(new Chunk(etiqueta + " ", ETIQUETA_FONT));
        p.add(new Chunk(valor != null ? valor : "-", TEXTO_FONT));
        return p;
    }

    private Paragraph crearBloqueEntrada(HistoriaClinica entrada) {
        Paragraph bloque = new Paragraph();

        Paragraph fecha = new Paragraph("Date: " + entrada.getFecha().format(FECHA_FORMATO), ETIQUETA_FONT);
        fecha.setSpacingAfter(4);
        bloque.add(fecha);

        bloque.add(crearLineaDato("Diagnosis:", entrada.getDiagnostico()));

        if (entrada.getTratamientoRealizado() != null && !entrada.getTratamientoRealizado().isBlank()) {
            bloque.add(crearLineaDato("Treatment performed:", entrada.getTratamientoRealizado()));
        }

        if (entrada.getObservaciones() != null && !entrada.getObservaciones().isBlank()) {
            bloque.add(crearLineaDato("Observations:", entrada.getObservaciones()));
        }

        if (entrada.getCreadoPor() != null) {
            Paragraph creador = new Paragraph("Recorded by: " + entrada.getCreadoPor(), SUBTITULO_FONT);
            creador.setSpacingBefore(4);
            bloque.add(creador);
        }

        bloque.add(new LineSeparator());

        return bloque;
    }
}