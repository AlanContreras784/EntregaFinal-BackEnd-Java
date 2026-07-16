package com.techlab.productos_ecologicos.controllers;

import java.util.List;

import com.techlab.productos_ecologicos.dto.ApiResponse;
import com.techlab.productos_ecologicos.dto.ProductoRequestDTO;
import com.techlab.productos_ecologicos.dto.ProductoResponseDTO;
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

import com.techlab.productos_ecologicos.services.ProductoService;

import jakarta.validation.Valid;

@RestController 
@RequestMapping("/productos")   
public class ProductoController {

    private final ProductoService service;
    
    public ProductoController(ProductoService service) {
        this.service = service;
    }
    // Obtiene todos los productos y devuelve una respuesta estándar para el frontend.
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductoResponseDTO>>> listarProductos() {
        List<ProductoResponseDTO> productos =
                service.obtenerProductosResponse();
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Productos obtenidos correctamente.",
                        productos
                )
        );
    }

    // Obtiene un producto por id y devuelve una respuesta estándar para el frontend.
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductoResponseDTO>> obtenerProducto(
            @PathVariable("id") Integer id) {
        ProductoResponseDTO producto =
                service.obtenerProductoResponse(id);
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Producto obtenido correctamente.",
                        producto
                )
        );
    }
    // Busca productos por nombre y devuelve una respuesta estándar para el frontend.
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<ApiResponse<List<ProductoResponseDTO>>> buscarPorNombre(
            @PathVariable("nombre") String nombre) {
        List<ProductoResponseDTO> productos =
                service.buscarPorNombreResponse(nombre);
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Productos encontrados.",
                        productos
                )
        );
    }

    // Busca productos por categoría y devuelve una respuesta estándar para el frontend.
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<ApiResponse<List<ProductoResponseDTO>>> buscarPorCategoria(
            @PathVariable("categoria") String categoria) {
        List<ProductoResponseDTO> productos =
                service.buscarPorCategoriaResponse(categoria);
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Productos encontrados.",
                        productos
                )
        );
    }
    // Crea un producto nuevo utilizando ProductoRequestDTO.
    // El Service se encarga de convertir el DTO en entidad,
    // buscar la categoría y guardar el producto.
    // Crea un nuevo producto y devuelve una respuesta estándar para el frontend.
    @PostMapping
    public ResponseEntity<ApiResponse<ProductoResponseDTO>> crearProducto(
            @Valid @RequestBody ProductoRequestDTO dto) {
        ProductoResponseDTO producto =
                service.crearProducto(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        new ApiResponse<>(
                                true,
                                "Producto creado correctamente.",
                                producto
                        )
                );
    }

    // Actualiza un producto existente utilizando ProductoRequestDTO.
    // Busca el producto existente, modifica sus datos
    // y devuelve la respuesta preparada para el frontend.
    // Actualiza un producto existente y devuelve una respuesta estándar para el frontend.
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductoResponseDTO>> actualizarProducto(
            @PathVariable("id") Integer id,
            @Valid @RequestBody ProductoRequestDTO dto) {

        ProductoResponseDTO producto =
                service.actualizarProducto(id, dto);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Producto actualizado correctamente.",
                        producto
                )
        );
    }
    // Elimina un producto y devuelve una respuesta estándar para el frontend.
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarProducto(
            @PathVariable("id") Integer id) {

        service.eliminar(id);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Producto eliminado correctamente.",
                        null
                )
        );
    }
}