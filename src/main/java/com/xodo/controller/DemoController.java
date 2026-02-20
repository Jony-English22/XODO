package com.xodo.controller;

import com.fasterxml.jackson.databind.ObjectMapper; // Necesario para convertir manual
import com.xodo.model.Producto;
import com.xodo.model.SugerenciasResponse;
import com.xodo.model.VentaRequest;
import com.xodo.model.VentaResponse;
import com.xodo.service.AiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin("*")
public class DemoController {

    @Autowired
    private AiService aiService;

    // Herramienta para leer JSON manualmente
    private final ObjectMapper mapper = new ObjectMapper();

    @PostMapping("/procesar")
    // Ahora usamos VentaRequest en lugar de Map o String para ser más ordenados
    public ResponseEntity<VentaResponse> procesarVenta(@RequestBody VentaRequest request) {

        try {
            // CASO 1: VIENE UNA IMAGEN
            if (request.getImagenBase64() != null && !request.getImagenBase64().isEmpty()) {
                System.out.println("📸 Recibida imagen...");
                VentaResponse respuesta = aiService.interpretarImagen(request.getImagenBase64());
                return ResponseEntity.ok(respuesta);
            }

            // CASO 2: VIENE TEXTO
            else if (request.getTexto() != null && !request.getTexto().isEmpty()) {
                System.out.println("📩 Recibido texto: " + request.getTexto());
                VentaResponse respuesta = aiService.interpretarVenta(request.getTexto());
                return ResponseEntity.ok(respuesta);
            }

            return ResponseEntity.badRequest().build();

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/sugerencias")
    public ResponseEntity<SugerenciasResponse> sugerencias(@RequestParam(defaultValue = "negocio general") String contexto) {
        return ResponseEntity.ok(aiService.sugerirComandos(contexto));
    }

    @GetMapping("/cambiar-negocio")
    public ResponseEntity<String> cambiarNegocio(@RequestParam String tipo) {
        System.out.println("🎛️ Switch Frontend: " + tipo);

        // Llamamos al servicio para filtrar H2 y el Prompt
        aiService.cambiarContexto(tipo.toUpperCase());

        return ResponseEntity.ok("Contexto actualizado a " + tipo);
    }
}