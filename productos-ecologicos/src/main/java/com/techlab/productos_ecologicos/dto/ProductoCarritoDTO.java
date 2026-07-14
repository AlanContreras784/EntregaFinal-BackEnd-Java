package com.techlab.productos_ecologicos.dto;

import lombok.Data;

@Data
public class ProductoCarritoDTO {

    private Integer id;
    private String nombre;
    private Double precio;
    private String imagenUrl;

}