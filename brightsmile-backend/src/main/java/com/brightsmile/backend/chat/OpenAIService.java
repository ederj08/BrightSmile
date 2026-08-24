package com.brightsmile.backend.chat;

import com.brightsmile.backend.cita.CitaRequest;
import com.brightsmile.backend.cita.CitaService;
import com.brightsmile.backend.servicio.Servicio;
import com.brightsmile.backend.servicio.ServicioService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.format.DateTimeFormatter;

@Service
public class OpenAIService {

    @Value("${openai.api.key:no-key}")
    private String apiKey;

    @Value("${openai.api.url:https://api.openai.com/v1/chat/completions}")
    private String apiUrl;

    @Value("${openai.model:gpt-4o-mini}")
    private String model;

    private final WebClient webClient;
    private final ServicioService servicioService;
    private final CitaService citaService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public OpenAIService(WebClient.Builder webClientBuilder,
                         ServicioService servicioService,
                         CitaService citaService) {
        this.webClient = webClientBuilder.build();
        this.servicioService = servicioService;
        this.citaService = citaService;
    }

    public String chat(String userMessage, List<Map<String, String>> conversationHistory) {
        List<Map<String, Object>> messages = new ArrayList<>();

        Map<String, Object> systemMessage = new HashMap<>();
        systemMessage.put("role", "system");
        systemMessage.put("content", buildSystemPrompt());
        messages.add(systemMessage);

        for (Map<String, String> entry : conversationHistory) {
            Map<String, Object> msg = new HashMap<>();
            msg.put("role", entry.get("role"));
            msg.put("content", entry.get("content"));
            messages.add(msg);
        }

        Map<String, Object> userMsg = new HashMap<>();
        userMsg.put("role", "user");
        userMsg.put("content", userMessage);
        messages.add(userMsg);

        // Bucle de function calling: máximo 4 vueltas para evitar loops infinitos
        for (int i = 0; i < 4; i++) {
            JsonNode response = llamarOpenAI(messages);
            JsonNode choice = response.path("choices").get(0);
            JsonNode message = choice.path("message");

            JsonNode toolCalls = message.path("tool_calls");

            if (toolCalls.isMissingNode() || !toolCalls.isArray() || toolCalls.size() == 0) {
                // No pidió ninguna función — esta es la respuesta final
                return message.path("content").asText();
            }

            // El modelo pidió ejecutar una o más funciones
            Map<String, Object> assistantMsg = new HashMap<>();
            assistantMsg.put("role", "assistant");
            assistantMsg.put("content", message.path("content").isNull() ? null : message.path("content").asText(null));
            assistantMsg.put("tool_calls", objectMapper.convertValue(toolCalls, Object.class));
            messages.add(assistantMsg);

            for (JsonNode toolCall : toolCalls) {
                String toolCallId = toolCall.path("id").asText();
                String functionName = toolCall.path("function").path("name").asText();
                String argumentsJson = toolCall.path("function").path("arguments").asText();

                String resultado = ejecutarFuncion(functionName, argumentsJson);

                Map<String, Object> toolMsg = new HashMap<>();
                toolMsg.put("role", "tool");
                toolMsg.put("tool_call_id", toolCallId);
                toolMsg.put("content", resultado);
                messages.add(toolMsg);
            }
        }

        return "Lo siento, hubo un problema procesando tu solicitud. Por favor llámanos directamente a la clínica.";
    }

    private JsonNode llamarOpenAI(List<Map<String, Object>> messages) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", model);
        requestBody.put("messages", messages);
        requestBody.put("tools", construirDefinicionesDeHerramientas());
        requestBody.put("max_tokens", 500);
        requestBody.put("temperature", 0.7);

        try {
            Map response = webClient.post()
                    .uri(apiUrl)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            return objectMapper.valueToTree(response);
        } catch (Exception e) {
            throw new RuntimeException("Error al llamar a OpenAI", e);
        }
    }

    // Ejecuta la función pedida por el modelo y devuelve el resultado como texto/JSON
    private String ejecutarFuncion(String nombre, String argumentosJson) {
        try {
            if ("listar_servicios".equals(nombre)) {
                return listarServiciosJson();
            }

            if ("crear_cita".equals(nombre)) {
                return crearCitaDesdeArgumentos(argumentosJson);
            }

            return "{\"error\": \"Función desconocida: " + nombre + "\"}";
        } catch (Exception e) {
            return "{\"error\": \"" + escaparJson(e.getMessage()) + "\"}";
        }
    }

    private String listarServiciosJson() {
        List<Servicio> servicios = servicioService.getServiciosActivos();
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < servicios.size(); i++) {
            Servicio s = servicios.get(i);
            if (i > 0) sb.append(",");
            sb.append("{")
                    .append("\"id\":").append(s.getId()).append(",")
                    .append("\"nombre\":\"").append(escaparJson(s.getNombre())).append("\",")
                    .append("}");
        }
        sb.append("]");
        return sb.toString();
    }

    private String crearCitaDesdeArgumentos(String argumentosJson) {
        try {
            JsonNode args = objectMapper.readTree(argumentosJson);

            CitaRequest request = new CitaRequest();
            request.setPacienteNombre(args.path("pacienteNombre").asText());
            request.setPacienteEmail(args.path("pacienteEmail").asText());
            request.setPacienteTelefono(args.path("pacienteTelefono").asText());
            request.setTipoDocumento(args.path("tipoDocumento").asText());
            request.setNumeroDocumento(args.path("numeroDocumento").asText());
            request.setServicioId(args.path("servicioId").asLong());
            request.setFecha(LocalDate.parse(args.path("fecha").asText()));

            String horaTexto = args.path("hora").asText();
            if (horaTexto.length() == 5) {
                horaTexto = horaTexto + ":00";
            }
            request.setHora(LocalTime.parse(horaTexto));

            if (args.hasNonNull("notas")) {
                request.setNotas(args.path("notas").asText());
            }

            var citaCreada = citaService.crearCita(request);

            return "{\"exito\": true, \"citaId\": " + citaCreada.getId() +
                    ", \"mensaje\": \"Cita creada correctamente\"}";

        } catch (IllegalArgumentException e) {
            // Errores de validación de negocio (domingo, hora ocupada, etc.)
            return "{\"exito\": false, \"error\": \"" + escaparJson(e.getMessage()) + "\"}";
        } catch (Exception e) {
            return "{\"exito\": false, \"error\": \"No se pudo crear la cita, revisa los datos.\"}";
        }
    }

    private String escaparJson(String texto) {
        if (texto == null) return "";
        return texto.replace("\"", "'").replace("\n", " ");
    }

    // Define las funciones que el modelo puede invocar
    private List<Map<String, Object>> construirDefinicionesDeHerramientas() {
        List<Map<String, Object>> tools = new ArrayList<>();

        // Función 1: listar_servicios
        Map<String, Object> listarServiciosFn = new HashMap<>();
        listarServiciosFn.put("name", "listar_servicios");
        listarServiciosFn.put("description", "Obtiene la lista de servicios dentales disponibles con su id, nombre. Úsala siempre antes de agendar una cita para saber el id real del servicio.");
        listarServiciosFn.put("parameters", Map.of("type", "object", "properties", Map.of()));

        Map<String, Object> listarServiciosTool = new HashMap<>();
        listarServiciosTool.put("type", "function");
        listarServiciosTool.put("function", listarServiciosFn);
        tools.add(listarServiciosTool);

        // Función 2: crear_cita
        Map<String, Object> properties = new HashMap<>();
        properties.put("pacienteNombre", Map.of("type", "string", "description", "Nombre completo del paciente"));
        properties.put("pacienteEmail", Map.of("type", "string", "description", "Email del paciente"));
        properties.put("pacienteTelefono", Map.of("type", "string", "description", "Teléfono del paciente"));
        properties.put("tipoDocumento", Map.of("type", "string", "description", "Tipo de documento: PASSPORT, NATTIONAL_ID, DRIVER_LICENSE u OTHER"));
        properties.put("numeroDocumento", Map.of("type", "string", "description", "Número de documento del paciente"));
        properties.put("servicioId", Map.of("type", "integer", "description", "Id del servicio, obtenido de listar_servicios"));
        properties.put("fecha", Map.of("type", "string", "description", "Fecha completa de la cita en formato YYYY-MM-DD. " + "Si el paciente indicó día y mes sin año, usa la próxima ocurrencia futura " + "de esa fecha según la fecha actual proporcionada en el system prompt."));
        properties.put("hora", Map.of("type", "string", "description", "Hora de la cita en formato HH:mm, ej. 09:00"));
        properties.put("notas", Map.of("type", "string", "description", "Notas adicionales, opcional"));

        Map<String, Object> crearCitaFn = new HashMap<>();
        crearCitaFn.put("name", "crear_cita");
        crearCitaFn.put("description", "Crea una cita dental en el sistema. " +
                        "Solo debe utilizarse después de que el paciente haya confirmado explícitamente " +
                        "todos los datos de la cita. La fecha debe ser una fecha futura válida.");
        crearCitaFn.put("parameters", Map.of(
                "type", "object",
                "properties", properties,
                "required", List.of("pacienteNombre", "pacienteEmail", "pacienteTelefono",
                        "tipoDocumento", "numeroDocumento", "servicioId", "fecha", "hora")
        ));

        Map<String, Object> crearCitaTool = new HashMap<>();
        crearCitaTool.put("type", "function");
        crearCitaTool.put("function", crearCitaFn);
        tools.add(crearCitaTool);

        return tools;
    }

    private String buildSystemPrompt() {
        LocalDate hoy = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE d 'de' MMMM 'de' yyyy",new java.util.Locale("es","ES"));
        String fechaHoy = hoy.format(formatter);

        return """
               Eres el asistente virtual de BrightSmile Dental, una clínica dental profesional.
            Tu nombre es Bright y tu objetivo es ayudar a los pacientes a:
            1. Agendar citas dentales (creándolas de verdad en el sistema)
            2. Responder preguntas sobre los servicios de la clínica
            3. Informar sobre horarios y disponibilidad
            4. Proporcionar información general sobre cuidado dental.
            
                IMPORTANTE: La fecha de HOY es %s (formato ISO: %s). Usa siempre esta fecha real
                            como referencia — nunca inventes ni asumas otra fecha. Cuando el paciente pida
                            una fecha relativa como "mañana" o "el próximo lunes", calcúlala a partir de esta fecha real.

            Información de la clínica:
            - Nombre: BrightSmile Dental
            - Dirección: Bucaramanga, Colombia.
            - Teléfono: 46972268.
            - Horario: Lunes a viernes, 8:00 AM - 5:00 PM, sábados ,8:00 AM - 1:00 PM (cerrado los domingos).
            - Profesionales: contamos con 3 odontólogos especializados en distintas áreas
            - Especialidades: limpieza dental, blanqueamiento, ortodoncia, extracciones, endodoncias, coronas.
            
          
            Reglas de conversación:
            - Nunca hagas más de una pregunta por mensaje.
            - Primero identifica el servicio que quiere el paciente.
            - Una vez elegido el servicio, solicita los datos necesarios para la cita.
            - Puedes solicitar varios datos relacionados en un mismo mensaje cuando sea conveniente.
            - No vuelvas a solicitar información que el paciente ya proporcionó.
           
            Reglas para agendar una cita:
            - Antes de agendar, asegúrate de tener:
              nombre completo, email, teléfono, tipo de documento,
              número de documento, servicio, fecha y hora.
            - Si falta información, solicita únicamente la información faltante.
            - Nunca inventes un servicioId.
            - Siempre usa listar_servicios antes de seleccionar el servicioId.
            - Cuando tengas todos los datos, muestra un resumen de la cita y pide confirmación.
            - Solo después de recibir una confirmación explícita del paciente,
             utiliza crear_cita.

            Reglas generales:
            - Siempre sé amable y profesional.
            - No inventes información que no tienes — si no sabes algo, dile al paciente que lo consulte
              llamando directamente a la clínica.
            - Responde siempre en el mismo idioma que el paciente use.
            - Mantén respuestas concisas
            """.formatted(fechaHoy,hoy.toString());
    }
}