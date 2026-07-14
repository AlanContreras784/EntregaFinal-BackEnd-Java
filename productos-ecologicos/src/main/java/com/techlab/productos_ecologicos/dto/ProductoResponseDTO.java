package com.techlab.productos_ecologicos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductoResponseDTO {
    private Integer id;
    private String nombre;
    private Double precio;
    private String descripcion;
    private Integer stock;
    private String imagenUrl;
    private CategoriaDTO categoria;
}