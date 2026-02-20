package com.xodo.model;
import com.fasterxml.jackson.annotation.JsonProperty;

public class VentaRequest {
    @JsonProperty("texto")
    private String texto;

    @JsonProperty("imagenBase64") // Nuevo campo
    private String imagenBase64;

    public VentaRequest() {}

    // Getters y Setters
    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }
    public String getImagenBase64() { return imagenBase64; }
    public void setImagenBase64(String imagenBase64) { this.imagenBase64 = imagenBase64; }
}