package com.techlab.productos_ecologicos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.CommandLineRunner;

import com.techlab.productos_ecologicos.models.Categoria;
import com.techlab.productos_ecologicos.models.Producto;
import com.techlab.productos_ecologicos.services.CategoriaService;
import com.techlab.productos_ecologicos.services.ProductoService;


@SpringBootApplication
public class ProductosEcologicosApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductosEcologicosApplication.class, args);
	}	
		@Bean
    CommandLineRunner cargarDatos(ProductoService productoService, CategoriaService categoriaService) {
        return args -> {
            if (categoriaService.listarTodos().isEmpty()) {
                Categoria almacen = categoriaService.guardar(new Categoria(null, "Almacén", "Productos de almacén"));
                Categoria bebidas = categoriaService.guardar(new Categoria(null, "Bebidas", "Bebidas y líquidos"));
				Categoria limpieza = categoriaService.guardar(new Categoria(null, "Limpieza", "Productos de limpieza"));
				Categoria electronica = categoriaService.guardar(new Categoria(null, "Electrónica", "Productos electrónicos"));

                productoService.guardar(new Producto(null, "Yerba 1kg", 3200, "Producto natural", 50, almacen));
                productoService.guardar(new Producto(null, "Aceite 1.5L", 4100, "Aceite de oliva", 30, almacen));
                productoService.guardar(new Producto(null, "Agua 2L", 900, "Agua mineral", 80, bebidas));
                productoService.guardar(new Producto(null, "Detergente", 1200, "Detergente para lavandería", 40, limpieza));
                productoService.guardar(new Producto(null, "Smartphone", 800, "Smartphone con cámara de alta calidad", 20, electronica));
				productoService.guardar(new Producto(null, "Laptop", 1500, "Laptop de alto rendimiento", 10, electronica));
            }
        };
    }
}
