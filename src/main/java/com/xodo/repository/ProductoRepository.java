package com.xodo.repository;

import com.xodo.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    /**
     * Búsqueda inteligente: encuentra productos por nombre O sinónimos.
     * Ejemplo: buscarPorTermino("chesco") → Coca Cola 600ml
     */
    @Query("SELECT p FROM Producto p WHERE " +
           "LOWER(p.keywords) LIKE LOWER(CONCAT('%', :termino, '%')) OR " +
           "LOWER(p.nombre) LIKE LOWER(CONCAT('%', :termino, '%'))")
    List<Producto> buscarPorTermino(@Param("termino") String termino);

    /**
     * Filtrar productos por categoría.
     * Ejemplo: findByCategoria("Bebidas") → [Coca Cola, Fanta, ...]
     */
    List<Producto> findByCategoria(String categoria);

    /**
     * Filtrar productos por unidad de medida.
     * Ejemplo: findByUnidadBase("m2") → [Lona Front, Vinil Adhesivo]
     */
    List<Producto> findByUnidadBase(String unidadBase);

    /**
     * Buscar producto exacto por nombre.
     * Ejemplo: findByNombreIgnoreCase("cemento cruz azul") → Optional<Producto>
     */
    Optional<Producto> findByNombreIgnoreCase(String nombre);
} 

