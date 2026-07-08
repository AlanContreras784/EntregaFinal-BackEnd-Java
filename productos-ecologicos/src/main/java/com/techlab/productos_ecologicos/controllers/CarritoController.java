package com.techlab.productos_ecologicos.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.techlab.productos_ecologicos.dto.CarritoResumenDTO;
import com.techlab.productos_ecologicos.models.Carrito;
import com.techlab.productos_ecologicos.services.CarritoService;

@RestController
@RequestMapping("/carritos")
public class CarritoController {

    private final CarritoService service;

    public CarritoController(CarritoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Carrito>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Carrito> obtenerCarrito(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    // Devuelve el precio total de todos los productos en el carrito
    @GetMapping("/{id}/resumen")
    public ResponseEntity<CarritoResumenDTO> resumen(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(service.obtenerResumen(id));
    }

    // Crea un carrito vacío — debe existir antes de agregar productos
    @PostMapping
    public ResponseEntity<Carrito> crear() {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear());
    }

    // Dos @PathVariable porque necesita identificar el carrito y el producto.
    // No usa @RequestBody — toda la información está en la URL.
    @PostMapping("/{carritoId}/productos/{productoId}")
    public ResponseEntity<Carrito> agregarProducto(
            @PathVariable("carritoId") Integer carritoId,
            @PathVariable("productoId") Integer productoId) {
        return ResponseEntity.ok(service.agregarProducto(carritoId, productoId));
    }

    // Descuenta un producto del carrito y devuelve la cantidad al stock
    @PutMapping("/{carritoId}/productos/{productoId}/descontar")
    public ResponseEntity<Carrito> descontarProducto(
            @PathVariable("carritoId") Integer carritoId,
            @PathVariable("productoId") Integer productoId) {
        return ResponseEntity.ok(service.descontarProducto(carritoId, productoId));
    }

    // Elimina un producto del carrito y devuelve la cantidad al stock
    @DeleteMapping("/{carritoId}/productos/{productoId}")
    public ResponseEntity<Carrito> eliminarProducto(
            @PathVariable("carritoId") Integer carritoId,
            @PathVariable("productoId") Integer productoId) {
        return ResponseEntity.ok(service.eliminarProducto(carritoId, productoId));
    }
    
    // Vacía el carrito sin eliminarlo — el usuario puede seguir usándolo
    @DeleteMapping("/{id}/vaciar")
    public ResponseEntity<Carrito> vaciar(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(service.vaciar(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Integer id) {
        service.eliminar(id);
        return ResponseEntity.ok().build();
    }
}