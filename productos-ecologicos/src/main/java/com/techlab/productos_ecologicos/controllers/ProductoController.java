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
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}") 
    public ResponseEntity<Producto> obtenerProducto(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @GetMapping("/nombre/{nombre}") 
    public ResponseEntity<List<Producto>> buscarPorNombre(@PathVariable("nombre") String nombre) {
        return ResponseEntity.ok(service.buscarPorNombre(nombre)); 
    }

    @GetMapping("/categoria/{categoria}") 
    public ResponseEntity<List<Producto>> buscarPorCategoria(@PathVariable("categoria") String categoria) {
        return ResponseEntity.ok(service.buscarPorCategoria(categoria));
    }

    @PostMapping("") 
    public ResponseEntity<Producto> crearProducto(@Valid @RequestBody Producto producto) {
        return ResponseEntity.ok(service.guardar(producto));
    }


    @PutMapping("/{id}") 
    public ResponseEntity<Producto> actualizarProducto(@PathVariable("id") Integer id, @Valid @RequestBody Producto datos) {
        return ResponseEntity.ok(service.actualizar(id, datos));
    }

    @DeleteMapping ("/{id}") 
    public ResponseEntity<Void> eliminarProducto(@PathVariable("id") Integer id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}