package com.xodo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class XodoApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(XodoApplication.class);

        // 2. OBLIGAR a que sea una aplicación Web Servlet (Tomcat)
        // Esto mata el conflicto con WebFlux/Netty
        app.setWebApplicationType(WebApplicationType.SERVLET);

        // 3. Arrancar
        app.run(args);
        System.out.println("==============================================");
        System.out.println("INICIANDO XODO - EL ERP CON IA DE GOOGLE");
        System.out.println("Backend corriendo en: http://localhost:8080");
        System.out.println("==============================================");
    }
}