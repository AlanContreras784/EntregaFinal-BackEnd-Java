package com.techlab.productos_ecologicos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import io.swagger.v3.oas.annotations.Hidden;


@RestControllerAdvice
@Hidden // <-- Esto le dice a Swagger: "ignora esta clase"
public class GlobalExceptionHandler {

    // Versión simplificada (solo devuelve el primer error):
    // String mensaje = ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
    // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mensaje);

    // Cuando @Valid falla, Spring lanza MethodArgumentNotValidException.
    // Usamos Map porque pueden fallar varios campos a la vez:
    // { "nombre": "El nombre no puede estar vacío", "precio": "Debe ser mayor que cero" }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> manejarValidacion(
            MethodArgumentNotValidException ex) {

        Map<String, String> errores = new HashMap<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errores.put(error.getField(), error.getDefaultMessage());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errores);
    }

    @ExceptionHandler(ProductoNoEncontradoException.class)
    public ResponseEntity<String> manejarProductoNoEncontrado(
            ProductoNoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(CategoriaNoEncontradaException.class)
    public ResponseEntity<String> manejarCategoriaNoEncontrada(
            CategoriaNoEncontradaException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    // @ExceptionHandler(CategoriaNombreInvalidoException.class)
    // public ResponseEntity<String> manejarCategoriaNombreInvalido(
    //         CategoriaNombreInvalidoException ex) {
    //     return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    // }

    @ExceptionHandler(StockInsuficienteException.class)
    public ResponseEntity<String> manejarStockInsuficiente(
            StockInsuficienteException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(CarritoNoEncontradoException.class)
    public ResponseEntity<String> manejarCarritoNoEncontrado(
            CarritoNoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(ProductoNoEncontradoEnElCarritoException.class)
    public ResponseEntity<String> manejarProductoNoEncontradoEnElCarrito(
            ProductoNoEncontradoEnElCarritoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    // Se dispara cuando Hibernate intenta resolver (fetch EAGER) una relación
    // @ManyToOne cuyo id ya no existe en la base — por ejemplo, un CarritoProducto
    // que apunta a un producto que fue eliminado o recreado con otros ids.
    // Sin este handler, este caso caía en la excepción genérica y devolvía 500.
    @ExceptionHandler(jakarta.persistence.EntityNotFoundException.class)
    public ResponseEntity<String> manejarEntidadNoEncontrada(
            jakarta.persistence.EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                "El carrito contiene una referencia a un producto que ya no existe. " +
                "Vacíe el carrito o elimínelo y cree uno nuevo.");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> manejarExcepcionGenerica(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Ocurrió un error inesperado: " + ex.getMessage());
    }
}