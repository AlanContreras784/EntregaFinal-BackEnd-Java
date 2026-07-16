package com.techlab.productos_ecologicos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * Respuesta genérica utilizada por toda la API.
 *
 * Permite devolver siempre la misma estructura al frontend,
 * independientemente del endpoint invocado.
 *
 * success : indica si la operación fue exitosa.
 * message : mensaje descriptivo.
 * data    : información devuelta por la operación.
 * ¿Por qué usamos <T>? Es un tipo genérico. La misma clase sirve para cualquier dato.
 * Con productos:
ApiResponse<ProductoResponseDTO>
Con categorías:
ApiResponse<CategoriaDTO>
Con carrito:
ApiResponse<CarritoResponseDTO>
Con una lista:
ApiResponse<List<ProductoResponseDTO>>
Con un resumen:
ApiResponse<CarritoResumenDTO>
No necesitamos crear una clase distinta para cada respuesta.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    // Indica si la operación fue exitosa.
    private boolean success;

    // Mensaje descriptivo de la operación.
    private String message;

    // Datos devueltos por la API.
    private T data;
}