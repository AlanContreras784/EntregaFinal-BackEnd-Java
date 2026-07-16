package com.techlab.productos_ecologicos.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.techlab.productos_ecologicos.dto.ApiResponse;
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
        // Este endpoint es principalmente para administración.
        // Lista todos los carritos del sistema.
        // Devuelve DTOs para no exponer las entidades JPA.
        

        // Obtiene todos los carritos del sistema.
        // Devuelve una lista de DTOs para no exponer las entidades JPA.
        @GetMapping
        public ResponseEntity<ApiResponse<List<CarritoResponseDTO>>> listarTodos() {
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Carritos obtenidos correctamente.",
                        service.listarTodosDTO()
                )
        );
        }
                
        // Devuelve el carrito activo del usuario autenticado.
        // Utiliza un DTO para no exponer la entidad completa ni datos sensibles.
        @GetMapping("/mi-carrito")
        public ResponseEntity<ApiResponse<CarritoResponseDTO>> obtenerMiCarrito() {
                CarritoResponseDTO carrito = service.obtenerMiCarritoDTO();
                return ResponseEntity.ok(
                        new ApiResponse<>(
                                true,
                                "Carrito obtenido correctamente.",
                                carrito
                        )
                );
        }

        // Devuelve un resumen del carrito:
        // cantidad de productos, subtotal, envío y total.
        @GetMapping("/mi-carrito/resumen")
        public ResponseEntity<ApiResponse<CarritoResumenDTO>> obtenerMiResumen() {
                CarritoResumenDTO resumen = service.obtenerResumen();
                return ResponseEntity.ok(
                        new ApiResponse<>(
                                true,
                                "Resumen del carrito obtenido correctamente.",
                                resumen
                        )
                );
        }

        // Crea un carrito vacío asociado al usuario autenticado.
        @PostMapping
        public ResponseEntity<ApiResponse<Carrito>> crear() {
                Carrito carrito = service.crear();
                return ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(
                                new ApiResponse<>(
                                        true,
                                        "Carrito creado correctamente.",
                                        carrito
                                )
                        );
        }
        
        // Agrega una unidad del producto indicado al carrito del usuario.
        // Devuelve el carrito actualizado mediante un DTO.
        public ResponseEntity<ApiResponse<CarritoResponseDTO>> agregarProducto(
                @PathVariable("productoId") Integer productoId) {
                CarritoResponseDTO carrito = service.agregarProductoDTO(productoId);
                return ResponseEntity.ok(
                        new ApiResponse<>(
                                true,
                                "Producto agregado al carrito correctamente.",
                                carrito
                        )
                );
        }
        // Descuenta una unidad del producto del carrito.
        // Si la cantidad llega a cero, elimina el producto del carrito.
        @PutMapping("/productos/{productoId}/descontar")
        public ResponseEntity<ApiResponse<CarritoResponseDTO>> descontarProducto(
                @PathVariable("productoId") Integer productoId) {
                CarritoResponseDTO carrito = service.descontarProductoDTO(productoId);
                return ResponseEntity.ok(
                        new ApiResponse<>(
                                true,
                                "Producto descontado correctamente.",
                                carrito
                        )
                );
        }
        // Elimina completamente un producto del carrito,
        // devolviendo todas sus unidades al stock.
        @DeleteMapping("/productos/{productoId}")
        public ResponseEntity<ApiResponse<CarritoResponseDTO>> eliminarProducto(
                @PathVariable("productoId") Integer productoId) {
                CarritoResponseDTO carrito = service.eliminarProductoDTO(productoId);
                return ResponseEntity.ok(
                        new ApiResponse<>(
                                true,
                                "Producto eliminado del carrito correctamente.",
                                carrito
                        )
                );
        }
        // Vacía el carrito del usuario y devuelve todas las unidades al stock.
        // El carrito sigue existiendo, simplemente queda sin productos.
        // Vacía el carrito del usuario.
        @DeleteMapping("/mi-carrito/vaciar")
        public ResponseEntity<ApiResponse<CarritoResponseDTO>> vaciar() {
                CarritoResponseDTO carrito = service.vaciarDTO();
                return ResponseEntity.ok(
                        new ApiResponse<>(
                                true,
                                "Carrito vaciado correctamente.",
                                carrito
                        )
                );
        }

        // Elimina completamente un carrito por su id.
        // Este endpoint es más útil para administración que para un usuario final.
        @DeleteMapping("/{id}")
        public ResponseEntity<ApiResponse<Void>> eliminar(
                @PathVariable("id") Integer id) {
                service.eliminar(id);
                return ResponseEntity.ok(
                        new ApiResponse<>(
                                true,
                                "Carrito eliminado correctamente.",
                                null
                        )
                );
        }
    
}