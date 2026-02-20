package com.xodo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VentaResponse {

    // ==========================================
    // SECCIÓN 1: VISTA MICRO (TIENDITA / RÁPIDA)
    // ==========================================
    private String resumenHumano;  // "✅ Venta registrada: 2 Cocas, 1 Sabritas"
    private Double total;          // $51.00
    private String status;         // "EXITO" | "CONFIRMAR_PRECIO"

    // ==========================================
    // SECCIÓN 2: VISTA CORPORATIVA (DETALLE ERP)
    // ==========================================
    private String folio;          // "V-20231027-001"
    private String cliente;        // "Público en General" (o nombre detectado)
    private String metodoPago;     // "Efectivo" | "Crédito"
    private Double subtotal;       // $43.96
    private Double ivaTotal;       // $7.04
    
    // Lista de productos desglosados (Tabla del Dashboard)
    private List<DetalleItem> items;

    // ==========================================
    // CLASE INTERNA PARA LOS ITEMS DE LA TABLA
    // ==========================================
    public static class DetalleItem {
        private String sku;            // "REF-600ML"
        private String descripcion;    // "Coca Cola 600ml NR"
        private Double cantidad;       // 2.0
        private String unidad;         // "pz"
        private Double precioUnitario; // $18.00
        private Double importe;        // $36.00
        private String claveSat;       // "50202306"

        // Constructor vacío
        public DetalleItem() {}

        // Getters y Setters de DetalleItem
        public String getSku() { return sku; }
        public void setSku(String sku) { this.sku = sku; }
        public String getDescripcion() { return descripcion; }
        public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
        public Double getCantidad() { return cantidad; }
        public void setCantidad(Double cantidad) { this.cantidad = cantidad; }
        public String getUnidad() { return unidad; }
        public void setUnidad(String unidad) { this.unidad = unidad; }
        public Double getPrecioUnitario() { return precioUnitario; }
        public void setPrecioUnitario(Double precioUnitario) { this.precioUnitario = precioUnitario; }
        public Double getImporte() { return importe; }
        public void setImporte(Double importe) { this.importe = importe; }
        public String getClaveSat() { return claveSat; }
        public void setClaveSat(String claveSat) { this.claveSat = claveSat; }
    }

    // ==========================================
    // GETTERS Y SETTERS DE VentaResponse
    // ==========================================

    public String getResumenHumano() { return resumenHumano; }
    public void setResumenHumano(String resumenHumano) { this.resumenHumano = resumenHumano; }

    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getFolio() { return folio; }
    public void setFolio(String folio) { this.folio = folio; }

    public String getCliente() { return cliente; }
    public void setCliente(String cliente) { this.cliente = cliente; }

    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }

    public Double getSubtotal() { return subtotal; }
    public void setSubtotal(Double subtotal) { this.subtotal = subtotal; }

    public Double getIvaTotal() { return ivaTotal; }
    public void setIvaTotal(Double ivaTotal) { this.ivaTotal = ivaTotal; }

    public List<DetalleItem> getItems() { return items; }
    public void setItems(List<DetalleItem> items) { this.items = items; }
}