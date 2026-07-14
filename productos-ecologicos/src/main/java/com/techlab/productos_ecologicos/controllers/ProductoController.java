package com.techlab.productos_ecologicos.controllers;

import java.util.List;

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

    // Obtiene todos los productos y devuelve solamente los datos necesarios para el frontend.
    @GetMapping
    public ResponseEntity<List<ProductoResponseDTO>> listarProductos() {
        return ResponseEntity.ok(service.obtenerProductosResponse());
    }

    // Obtiene un producto por id y devuelve un DTO evitando exponer la entidad JPA.
    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> obtenerProducto(
            @PathVariable("id")  Integer id) {
        return ResponseEntity.ok(service.obtenerProductoResponse(id));
    }

    // Busca productos por nombre y devuelve una lista de DTOs preparados para el frontend.
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<List<ProductoResponseDTO>> buscarPorNombre(
            @PathVariable("nombre") String nombre) {

        return ResponseEntity.ok(
                service.buscarPorNombreResponse(nombre)
        );
    }

   // Busca productos por categoría y devuelve una lista de DTOs preparados para el frontend.
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<ProductoResponseDTO>> buscarPorCategoria(
            @PathVariable("categoria") String categoria) {

        return ResponseEntity.ok(
                service.buscarPorCategoriaResponse(categoria)
        );
    }
    // Crea un producto nuevo utilizando ProductoRequestDTO.
    // El Service se encarga de convertir el DTO en entidad,
    // buscar la categoría y guardar el producto.
    @PostMapping
    public ResponseEntity<ProductoResponseDTO> crearProducto(
            @Valid @RequestBody ProductoRequestDTO dto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.crearProducto(dto));
    }

        /// Actualiza un producto existente utilizando ProductoRequestDTO.
    // Busca el producto existente, modifica sus datos
    // y devuelve la respuesta preparada para el frontend.
    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> actualizarProducto(
            @PathVariable("id") Integer id,
            @Valid @RequestBody ProductoRequestDTO dto) {

        return ResponseEntity.ok(
                service.actualizarProducto(id, dto)
        );
    }
    // Elimina un producto por id y devuelve una respuesta sin contenido.
    @DeleteMapping ("/{id}") 
    public ResponseEntity<Void> eliminarProducto(@PathVariable("id") Integer id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}