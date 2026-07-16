package com.techlab.productos_ecologicos.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiError {
    // Indica si la operación fue exitosa.
    private boolean success;
    // Código HTTP (404, 400, 500, etc.).
    private int status;
    // Nombre del estado HTTP (NOT_FOUND, BAD_REQUEST, etc.).
    private String error;
    // Mensaje descriptivo del error.
    private String message;
    // Información adicional (por ejemplo errores de validación).
    private Object errors;
    // Fecha y hora en que ocurrió el error.
    private LocalDateTime timestamp;
}