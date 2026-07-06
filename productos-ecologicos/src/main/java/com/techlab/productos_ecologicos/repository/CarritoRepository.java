package com.techlab.productos_ecologicos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techlab.productos_ecologicos.models.Carrito;

public interface  CarritoRepository extends JpaRepository<Carrito, Integer> {
    
}
