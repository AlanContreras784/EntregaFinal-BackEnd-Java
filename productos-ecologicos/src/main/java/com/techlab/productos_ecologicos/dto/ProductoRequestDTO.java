package com.techlab.productos_ecologicos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductoRequestDTO {

    // Nombre que tendrá el producto
    @NotBlank(message = "El nombre del producto no puede estar vacío")
    private String nombre;

    // Precio del producto
    @NotNull(message = "El precio no puede estar vacío")
    @Positive(message = "El precio debe ser mayor que cero")
    private Double precio;

    // Descripción del producto
    @NotBlank(message = "La descripción no puede estar vacía")
    private String descripcion;

    // Cantidad disponible en stock
    @PositiveOrZero(message = "El stock no puede ser negativo")
    private Integer stock;

    // URL de la imagen del producto
    private String imagenUrl;

    // Solamente recibimos el ID de la categoría.
    // No enviamos la entidad Categoria completa.
    @NotNull(message = "La categoría es obligatoria")
    private Integer categoriaId;
}