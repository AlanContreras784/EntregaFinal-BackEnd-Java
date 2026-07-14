package com.techlab.productos_ecologicos.dto;

import lombok.Data;

@Data
public class CarritoProductoDTO {

    private Integer id;
    private Integer cantidad;
    private ProductoCarritoDTO producto;

}