package com.techlab.productos_ecologicos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techlab.productos_ecologicos.models.Categoria;
import com.techlab.productos_ecologicos.models.Producto;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
  
     List<Producto> findByNombreContaining(String nombre);
} 

