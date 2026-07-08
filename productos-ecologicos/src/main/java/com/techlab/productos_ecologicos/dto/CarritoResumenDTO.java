package com.techlab.productos_ecologicos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarritoResumenDTO {
    private Integer cantidadProductos;
    private Double subtotal;
    private Double envio;
    private Double total;
}