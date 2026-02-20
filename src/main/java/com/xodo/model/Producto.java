package com.xodo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "productos")
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;          // "Coca Cola", "Cemento Cruz Azul", "Lona 13oz"
    private String categoria;       // "Bebidas", "Construcción", "Gran Formato"
    private Double precioBase;      // Precio por unidad base
    private String unidadBase;      // "pz", "kg", "m2"
    
    // El Secreto de la Adaptabilidad: Tags para la IA
    private String keywords;        // "chesco, refresco, soda", "gris, bulto", "impresion, vinil"
    
    // Datos Fiscales (Automáticos)
    private String claveSat;        // "50202306"
    private Double tasaIva;         // 0.16
    
    // Lógica de Negocio Específica (JSON simulado en String para H2)
    // Ejemplo Ferretería: {"densidad": 1.5, "venta_granel": true}
    // Ejemplo Imprenta: {"ancho_max": 3.20, "acabados": ["bastilla", "ojillos"]}
    @Column(length = 1000)
    private String configuracionExtra;

    public Producto() {}

    // CONSTRUCTOR COMPLETO (Para cargar datos iniciales)
    public Producto(Long id, String nombre, String categoria, Double precioBase, String unidadBase, String keywords, String claveSat, Double tasaIva, String configuracionExtra) {
        this.id = id;
        this.nombre = nombre;
        this.categoria = categoria;
        this.precioBase = precioBase;
        this.unidadBase = unidadBase;
        this.keywords = keywords;
        this.claveSat = claveSat;
        this.tasaIva = tasaIva;
        this.configuracionExtra = configuracionExtra;
    }
    /*
        * Getters y Setters generados por Lombok (@Data)
     */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Double getPrecioBase() {
        return precioBase;
    }

    public void setPrecioBase(Double precioBase) {
        this.precioBase = precioBase;
    }

    public String getUnidadBase() {
        return unidadBase;
    }

    public void setUnidadBase(String unidadBase) {
        this.unidadBase = unidadBase;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getClaveSat() {
        return claveSat;
    }

    public void setClaveSat(String claveSat) {
        this.claveSat = claveSat;
    }

    public Double getTasaIva() {
        return tasaIva;
    }

    public void setTasaIva(Double tasaIva) {
        this.tasaIva = tasaIva;
    }

    public String getConfiguracionExtra() {
        return configuracionExtra;
    }

    public void setConfiguracionExtra(String configuracionExtra) {
        this.configuracionExtra = configuracionExtra;
    }
}