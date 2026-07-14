package com.techlab.productos_ecologicos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
// Representa la información de una categoría que será enviada al cliente.
// Este DTO se utiliza como respuesta de la API (ResponseDTO),
// evitando exponer directamente la entidad Categoria.
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoriaDTO {
    private Integer id;
    private String nombre;
    private String descripcion;
}