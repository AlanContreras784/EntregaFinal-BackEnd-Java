package com.techlab.productos_ecologicos.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.techlab.productos_ecologicos.dto.CarritoResponseDTO;
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

    // Lista todos los carritos del sistema.
    // Este endpoint es útil para administración o pruebas.
    // Por ahora devuelve entidades. Más adelante también podría devolver DTOs.
    @GetMapping
    public ResponseEntity<List<Carrito>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    // Devuelve el carrito activo del usuario autenticado.
    // Utiliza un DTO para no exponer la entidad completa ni datos sensibles.
    @GetMapping("/mi-carrito")
    public ResponseEntity<CarritoResponseDTO> obtenerMiCarrito() {
        return ResponseEntity.ok(service.obtenerMiCarritoDTO());
    }

    // Devuelve un resumen del carrito:
    // cantidad de productos, subtotal, envío y total.
    @GetMapping("/mi-carrito/resumen")
    public ResponseEntity<CarritoResumenDTO> obtenerMiResumen() {
        return ResponseEntity.ok(service.obtenerResumen());
    }

    // Crea un carrito vacío asociado al usuario autenticado.
    @PostMapping
    public ResponseEntity<Carrito> crear() {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.crear());
    }

    // Agrega una unidad del producto indicado al carrito del usuario.
    // Devuelve el carrito actualizado mediante un DTO.
    @PostMapping("/productos/{productoId}")
    public ResponseEntity<CarritoResponseDTO> agregarProducto(
            @PathVariable("productoId") Integer productoId) {

        return ResponseEntity.ok(service.agregarProductoDTO(productoId));
    }

    // Descuenta una unidad del producto del carrito.
    // Si la cantidad llega a cero, elimina el producto del carrito.
    @PutMapping("/productos/{productoId}/descontar")
    public ResponseEntity<CarritoResponseDTO> descontarProducto(
            @PathVariable("productoId") Integer productoId) {

        return ResponseEntity.ok(service.descontarProductoDTO(productoId));
    }

    // Elimina completamente un producto del carrito,
    // devolviendo todas sus unidades al stock.
    @DeleteMapping("/productos/{productoId}")
    public ResponseEntity<CarritoResponseDTO> eliminarProducto(
            @PathVariable("productoId") Integer productoId) {

        return ResponseEntity.ok(service.eliminarProductoDTO(productoId));
    }

    // Vacía el carrito del usuario y devuelve todas las unidades al stock.
    // El carrito sigue existiendo, simplemente queda sin productos.
    @DeleteMapping("/mi-carrito/vaciar")
    public ResponseEntity<CarritoResponseDTO> vaciar() {
        return ResponseEntity.ok(service.vaciarDTO());
    }

    // Elimina completamente un carrito por su id.
    // Este endpoint es más útil para administración que para un usuario final.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Integer id) {
        service.eliminar(id);
        return ResponseEntity.ok().build();
    }
}