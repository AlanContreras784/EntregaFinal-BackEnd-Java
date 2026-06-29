package com.techlab.productos_ecologicos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.techlab.productos_ecologicos.models.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    List<Producto> findByNombreContaining(String nombre);

    @Query("SELECT p FROM Producto p WHERE p.categoria.nombre = :nombreCategoria")
    List<Producto> buscarPorCategoria(@Param("nombreCategoria") String nombreCategoria);
}
