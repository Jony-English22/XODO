package com.xodo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.genai.Client;
import com.google.genai.types.Content;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.Part;
import com.google.genai.types.Blob;
import com.xodo.model.Producto;
import com.xodo.model.VentaResponse;
import com.xodo.model.SugerenciasResponse;
import com.xodo.repository.ProductoRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class AiService {

    @Value("${xodo.api.key}")
    private String geminiApiKey;

    @Autowired
    private ProductoRepository productoRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // OPTIMIZACIÓN 1: Cliente Único (Singleton)
    private Client client;

    private String negocioActual = "TIENDA";
    private String catalogoCacheado = "";

    @PostConstruct
    public void init() {
        // 1. Iniciar Cliente UNA SOLA VEZ
        this.client = Client.builder().apiKey(geminiApiKey).build();

        // 2. Cargar Catálogo
        cambiarContexto("TIENDA");

        // 3. OPTIMIZACIÓN 2: "Warm-up" (Calentamiento)
        // Hacemos una petición dummy en segundo plano para abrir la conexión SSL con Google.
        // Así, cuando el usuario hable, la "tubería" ya está abierta.
        new Thread(() -> {
            try {
                System.out.println("🔥 XODO: Calentando motores de IA...");
                client.models.generateContent("gemini-1.5-flash", "Hola", null);
                System.out.println("🚀 XODO: Motor IA listo y caliente.");
            } catch (Exception e) {
                System.err.println("⚠️ No se pudo calentar la IA (No afecta funcionamiento)");
            }
        }).start();
    }

    public List<Producto> cambiarContexto(String nuevoNegocio) {
        this.negocioActual = nuevoNegocio;
        List<Producto> productos = productoRepository.findByCategoria(nuevoNegocio);

        // OPTIMIZACIÓN 3: Prompt más corto = Respuesta más rápida
        this.catalogoCacheado = productos.stream()
                .map(p -> String.format("- %s ($%.0f) [%s]", p.getNombre(), p.getPrecioBase(), p.getUnidadBase()))
                .collect(Collectors.joining("\n"));

        return productos;
    }

    public VentaResponse interpretarVenta(String textoUsuario) {
        String prompt = String.format("""
            Rol: ERP (%s).
            CATÁLOGO:
            %s
            
            INPUT: "%s"
            
            TAREA: JSON Estricto venta.
            FORMATO: {"status":"EXITO","resumenHumano":"...","total":0.0,"items":[{"descripcion":"...","cantidad":0,"unidad":"pz","importe":0.0}]}
            """, negocioActual, catalogoCacheado, textoUsuario);

        return llamarGemini(prompt, "gemini-3-flash-preview", null);
    }

    public VentaResponse interpretarImagen(String base64Image) {
        try {
            String cleanBase64 = base64Image.contains(",") ? base64Image.split(",")[1] : base64Image;
            byte[] imageBytes = Base64.getDecoder().decode(cleanBase64);

            String prompt = String.format("""
                Rol: Cajero Visual (%s).
                CATÁLOGO:
                %s
                
                TAREA: Identifica producto. JSON Estricto.
                FORMATO: {"status":"EXITO","resumenHumano":"...","total":0.0,"items":[{"descripcion":"...","cantidad":1,"unidad":"pz","importe":0.0}]}
                """, negocioActual, catalogoCacheado);

            Blob imagenBlob = Blob.builder().data(imageBytes).mimeType("image/jpeg").build();
            return llamarGemini(prompt, "gemini-3-flash-preview", imagenBlob);

        } catch (Exception e) {
            e.printStackTrace();
            return crearError("Error imagen: " + e.getMessage());
        }
    }

    private VentaResponse llamarGemini(String prompt, String modelo, Blob imagen) {
        try {
            List<Part> partes = new ArrayList<>();
            partes.add(Part.builder().text(prompt).build());
            if (imagen != null) partes.add(Part.builder().inlineData(imagen).build());

            Content content = Content.builder().parts(partes).build();

            // Usamos el cliente ya instanciado (Mucho más rápido)
            GenerateContentResponse response = client.models.generateContent(modelo, content, null);

            String jsonRaw = response.text().replace("```json", "").replace("```", "").trim();
            int start = jsonRaw.indexOf("{");
            int end = jsonRaw.lastIndexOf("}");

            if (start != -1) {
                return objectMapper.readValue(jsonRaw.substring(start, end + 1), VentaResponse.class);
            }
            throw new RuntimeException("No JSON");
        } catch (Exception e) {
            e.printStackTrace();
            return crearError("Error técnico.");
        }
    }

    private VentaResponse crearError(String msg) {
        VentaResponse r = new VentaResponse(); r.setStatus("ERROR"); r.setResumenHumano(msg); return r;
    }

    public SugerenciasResponse sugerirComandos(String c) { return null; }
}