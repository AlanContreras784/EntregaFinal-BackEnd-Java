package com.techlab.productos_ecologicos.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.techlab.productos_ecologicos.exception.CategoriaNoEncontradaException;

import com.techlab.productos_ecologicos.models.Categoria;
import com.techlab.productos_ecologicos.repository.CategoriaRepository;

@Service // Anotación de Spring que marca esta clase como un "servicio". la ejecuta automaticamente al iniciar, sin esta anotacion Spring no sabe que es un servicio y no la inyecta donde se necesite.
public class CategoriaService {
    
     private final CategoriaRepository repository;

    public CategoriaService(CategoriaRepository repository) {
        this.repository = repository;
    }

    public Categoria guardar(Categoria c) {
        
        return repository.save(c);
    }

    public List<Categoria> listarTodos() {
        return repository.findAll();
    }

    public Categoria obtenerPorId(Integer id) {
        return repository.findById(id).orElseThrow(() -> new CategoriaNoEncontradaException("Categoría con ID " + id + " no encontrada"));
    }

    public Categoria actualizar(Integer id, Categoria datos) {
        Categoria existente = obtenerPorId(id);
        existente.setNombre(datos.getNombre());
        return existente;
    }

    public void eliminar(Integer id) {
        Categoria existente = obtenerPorId(id);
        repository.delete(existente);
    }   
}
