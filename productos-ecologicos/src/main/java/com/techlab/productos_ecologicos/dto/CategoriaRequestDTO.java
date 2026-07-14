package com.techlab.productos_ecologicos.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoriaRequestDTO {

    // Nombre de la categoría.
    @NotBlank(message = "El nombre de la categoría no puede estar vacío")
    private String nombre;

    // Descripción de la categoría.
    @NotBlank(message = "La descripción de la categoría no puede estar vacía")
    private String descripcion;

}