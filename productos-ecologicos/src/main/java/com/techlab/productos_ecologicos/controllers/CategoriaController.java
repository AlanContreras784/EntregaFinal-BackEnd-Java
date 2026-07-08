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

import com.techlab.productos_ecologicos.models.Categoria;
import com.techlab.productos_ecologicos.services.CategoriaService;

import jakarta.validation.Valid;


@RestController // Anotación de Spring que marca esta clase como un "controlador REST". la ejecuta automaticamente al iniciar, esta clase va a manejar request HTTP y las respuestas se van a serializar automáticamente a JSON. Sin esta anotación, Spring no sabe que es un controlador y no la mapea a las rutas HTTP correspondientes.
@RequestMapping("/categorias") // Ruta base para todas las operaciones relacionadas con categorias. Por ejemplo, GET /categorias devuelve la lista de categorias.

public class CategoriaController {
    private final CategoriaService service;// categoriaService no se crea con new, sino que se inyecta automáticamente por Spring gracias a la anotación @Service en la clase CategoriaService. Esto es parte del mecanismo de inyección de dependencias de Spring, que permite desacoplar las clases y facilitar el mantenimiento y las pruebas.   

    public CategoriaController(CategoriaService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Categoria>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Categoria> obtenerCategoria(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(service.obtenerPorId(id)); 
    }

    @PostMapping("")
    public ResponseEntity<Categoria> crearCategoria( @Valid @RequestBody Categoria categoria) {
        return ResponseEntity.ok(service.guardar(categoria));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Categoria> actualizarCategoria(@PathVariable("id") Integer id, @Valid @RequestBody Categoria categoria) {
        return ResponseEntity.ok(service.actualizar(id, categoria));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCategoria(@PathVariable("id") Integer id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}