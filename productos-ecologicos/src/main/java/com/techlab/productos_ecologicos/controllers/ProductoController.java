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
import org.springframework.http.ResponseEntity;

import com.techlab.productos_ecologicos.exception.ProductoNoEncontradoException;
import com.techlab.productos_ecologicos.models.Producto;
import com.techlab.productos_ecologicos.services.ProductoService;

import jakarta.validation.Valid;

@RestController 
@RequestMapping("/productos")   
public class ProductoController {

    private final ProductoService service;
    
    public ProductoController(ProductoService service) {
        this.service = service;
    }

    @GetMapping 

        public ResponseEntity<List<Producto>> listarTodos() {
            List<Producto> producto = service.listarTodos();
            return ResponseEntity.ok(producto);
        }

    @GetMapping("/{id}") 

        public ResponseEntity<Producto> obtenerProducto(@PathVariable int id) {
            try {
                Producto producto = service.obtenerPorId(id);
                return ResponseEntity.ok(producto);
            } catch (ProductoNoEncontradoException e) {
                return ResponseEntity.notFound().build();
            }
        }

    @GetMapping("/nombre/{nombre}") 

    public ResponseEntity<List<Producto>> buscarPorNombre(@PathVariable String nombre) {
        try{
            List<Producto> producto = service.buscarPorNombre(nombre);
            return ResponseEntity.ok(producto);
        } catch (ProductoNoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/categoria/{categoria}") 
    
    public ResponseEntity<List<Producto>> buscarPorCategoria(@PathVariable String categoria) {
        try {
            List<Producto> producto = service.buscarPorCategoria(categoria);
            return ResponseEntity.ok(producto);
        } catch (ProductoNoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("") 

    public ResponseEntity<Producto> crearProducto(@Valid @RequestBody Producto producto) {
        try {
            Producto nuevoProducto = service.guardar(producto);
            return ResponseEntity.ok(nuevoProducto);
        } catch (ProductoNoEncontradoException e) {
            return ResponseEntity.badRequest().build();
        }
    }


    @PutMapping("/{id}") 

        public ResponseEntity<Producto> actualizarProducto(@Valid @PathVariable int id, @RequestBody Producto datos) {
            try {
                Producto productoActualizado = service.actualizar(id, datos);
                return ResponseEntity.ok(productoActualizado);
            } catch (ProductoNoEncontradoException e) {
                return ResponseEntity.notFound().build();
            }
        }

    @DeleteMapping  ("/{id}") 
    
    public ResponseEntity<Void> eliminarProducto(@PathVariable int id) {
        try {
            service.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (ProductoNoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

