package com.techlab.productos_ecologicos.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.techlab.productos_ecologicos.dto.ApiResponse;
import com.techlab.productos_ecologicos.dto.CategoriaDTO;
import com.techlab.productos_ecologicos.dto.CategoriaRequestDTO;
//import com.techlab.productos_ecologicos.models.Categoria;
import com.techlab.productos_ecologicos.services.CategoriaService;


import jakarta.validation.Valid;


@RestController // Anotación de Spring que marca esta clase como un "controlador REST". la ejecuta automaticamente al iniciar, esta clase va a manejar request HTTP y las respuestas se van a serializar automáticamente a JSON. Sin esta anotación, Spring no sabe que es un controlador y no la mapea a las rutas HTTP correspondientes.
@RequestMapping("/categorias") // Ruta base para todas las operaciones relacionadas con categorias. Por ejemplo, GET /categorias devuelve la lista de categorias.

public class CategoriaController {
    private final CategoriaService service;// categoriaService no se crea con new, sino que se inyecta automáticamente por Spring gracias a la anotación @Service en la clase CategoriaService. Esto es parte del mecanismo de inyección de dependencias de Spring, que permite desacoplar las clases y facilitar el mantenimiento y las pruebas.   

    public CategoriaController(CategoriaService service) {
        this.service = service;
    }
    // Obtiene todas las categorías y devuelve una respuesta estándar para el frontend.
    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoriaDTO>>> listarTodos() {
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Categorías obtenidas correctamente.",
                        service.obtenerCategoriasResponse()
                )
        );
    }
    // Obtiene una categoría por id y devuelve una respuesta estándar para el frontend.
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoriaDTO>> obtenerCategoria(
            @PathVariable("id") Integer id) {
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Categoría obtenida correctamente.",
                        service.obtenerCategoriaResponse(id)
                )
        );
    }
    // Crea una nueva categoría y devuelve una respuesta estándar para el frontend.
    @PostMapping
    public ResponseEntity<ApiResponse<CategoriaDTO>> crearCategoria(
            @Valid @RequestBody CategoriaRequestDTO dto) {
        CategoriaDTO categoriaCreada = service.crearCategoria(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                    new ApiResponse<>(
                            true,
                            "Categoría creada correctamente.",
                            categoriaCreada
                    )
                );
    }
    // Actualiza una categoría existente y devuelve una respuesta estándar para el frontend.
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoriaDTO>> actualizarCategoria(
            @PathVariable("id") Integer id,
            @Valid @RequestBody CategoriaRequestDTO dto) {
        CategoriaDTO categoriaActualizada =
                service.actualizarCategoria(id, dto);
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Categoría actualizada correctamente.",
                        categoriaActualizada
                )
        );
    }
    // Elimina una categoría por id.
    // @DeleteMapping("/{id}")
    // public ResponseEntity<Void> eliminarCategoria(
    //         @PathVariable("id") Integer id) {
    //     service.eliminar(id);
    //     return ResponseEntity.noContent().build();
    // }

    // Elimina una categoría y devuelve una respuesta estándar para el frontend.
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarCategoria(
            @PathVariable("id") Integer id) {

        service.eliminar(id);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Categoría eliminada correctamente.",
                        null
                )
        );
    }
}