package com.xodo.config;

import com.xodo.model.Producto;
import com.xodo.repository.ProductoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(ProductoRepository repository) {
        return args -> {
            // Limpiamos la BD para no duplicar en reinicios
            repository.deleteAll();

            // Constructor: id, nombre, categoria, precio, unidad, keywords, sat, iva, config
            List<Producto> catalogo = List.of(
                    // TIENDA
                    new Producto(null, "Coca-Cola 600ml", "TIENDA", 18.00, "pz", "coca, coquita, refresco, chesco", "50202306", 0.16, null),
                    new Producto(null, "Sabritas Sal 45g", "TIENDA", 22.00, "pz", "papas, sabritas, botana", "50192100", 0.08, null),

                    // FERRETERIA
                    new Producto(null, "Cemento Gris Apasco", "FERRETERIA", 285.00, "saco", "cemento, bulto, gris, mezcla", "30111601", 0.16, "{\"peso_kg\": 50}"),
                    new Producto(null, "Clavo Estándar 2in", "FERRETERIA", 45.00, "kg", "clavos, clavo, fierro", "31162000", 0.16, null),

                    // IMPRENTA
                    new Producto(null, "Lona Front 13oz", "IMPRENTA", 120.00, "m2", "lona, impresion, publicidad", "82121500", 0.16, "{\"ancho_max\": 3.20}"),
                    new Producto(null, "Vinil Adhesivo", "IMPRENTA", 180.00, "m2", "vinil, sticker, calca", "82121500", 0.16, null)
            );

            repository.saveAll(catalogo);
            System.out.println("✅ XODO: Base de datos inicializada con " + repository.count() + " productos.");
        };
    }
}