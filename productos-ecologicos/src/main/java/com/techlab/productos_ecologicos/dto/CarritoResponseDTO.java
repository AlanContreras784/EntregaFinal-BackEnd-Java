package com.techlab.productos_ecologicos.dto;

import lombok.Data;

import java.util.List;

@Data
public class CarritoResponseDTO {

    private Integer id;
    private String estado;
    private UsuarioDTO usuario;
    private List<CarritoProductoDTO> productos;

}
