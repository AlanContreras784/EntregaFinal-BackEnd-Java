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
    CommandLineRunner cargarDatos(CategoriaService categoriaService,
                                  ProductoService productoService) {
        return args -> {
            if (categoriaService.listarTodos().isEmpty()) {

                Categoria ropa = categoriaService.guardar(
                    new Categoria(null, "Ropa masculina", "Indumentaria para hombre"));
                Categoria ropaFem = categoriaService.guardar(
                    new Categoria(null, "Ropa femenina", "Indumentaria para mujer"));
                Categoria joyeria = categoriaService.guardar(
                    new Categoria(null, "Joyería", "Anillos, collares y accesorios"));
                Categoria electronica = categoriaService.guardar(
                    new Categoria(null, "Electrónica", "Dispositivos y accesorios tecnológicos"));

                // Ropa masculina
                productoService.guardar(new Producto(null,
                    "Mochila plegable para notebook", 109.95, "Mochila plegable para notebook", 20,
                    "https://fakestoreapi.com/img/81fPKd-2AYL._AC_SL1500_t.png",
                    ropa));
                productoService.guardar(new Producto(null,
                    "Camiseta slim fit manga larga", 22.30, "Camiseta slim fit manga larga", 50,
                    "https://fakestoreapi.com/img/71-3HjGNDUL._AC_SY879._SX._UX._SY._UY_t.png",
                    ropa));
                productoService.guardar(new Producto(null,
                    "Campera de algodón", 55.99, "Campera de algodón", 30,
                    "https://fakestoreapi.com/img/71li-ujtlUL._AC_UX679_t.png",
                    ropa));
                productoService.guardar(new Producto(null,
                    "Pantalón casual slim fit", 15.99, "Pantalón casual slim fit", 40,
                    "https://fakestoreapi.com/img/71YXzeOuslL._AC_UY879_t.png",
                    ropa));

                // Joyería
                productoService.guardar(new Producto(null,
                    "Pulsera dragón oro y plata", 695.00,"Pulsera dragón oro y plata", 5,
                    "https://fakestoreapi.com/img/71pWzhdJNwL._AC_UL640_QL65_ML3_t.png",
                    joyeria));
                productoService.guardar(new Producto(null,
                    "Anillo de oro macizo micropavé", 168.00, "Anillo de oro macizo micropavé", 8,
                    "https://fakestoreapi.com/img/61sbMiUnoGL._AC_UL640_QL65_ML3_t.png",
                    joyeria));
                productoService.guardar(new Producto(null,
                    "Anillo de compromiso bañado en oro blanco", 9.99, "Anillo de compromiso bañado en oro blanco", 15,
                    "https://fakestoreapi.com/img/71YAIFU48IL._AC_UL640_QL65_ML3_t.png",
                    joyeria));
                productoService.guardar(new Producto(null,
                    "Aros dobles de acero inoxidable rosado", 10.99, "Aros dobles de acero inoxidable rosado",   12,
                    "https://fakestoreapi.com/img/51UDEzMJVpL._AC_UL640_QL65_ML3_t.png",
                    joyeria));

                // Electrónica
                productoService.guardar(new Producto(null,
                    "Disco externo portátil 2TB USB 3.0", 64.00, "Disco externo portátil 2TB USB 3.0", 25,
                    "https://fakestoreapi.com/img/61IBBVJvSDL._AC_SY879_t.png",
                    electronica));
                productoService.guardar(new Producto(null,
                    "SSD interno 1TB SATA III", 109.00, "SSD interno 1TB SATA III", 18,
                    "https://fakestoreapi.com/img/61U7T1koQqL._AC_SX679_t.png",
                    electronica));
                productoService.guardar(new Producto(null,
                    "SSD 256GB 3D NAND SATA III", 109.00, "SSD 256GB 3D NAND SATA III", 22,
                    "https://fakestoreapi.com/img/71kWymZ+c+L._AC_SX679_t.png",
                    electronica));
                productoService.guardar(new Producto(null,
                    "Disco externo 4TB para PlayStation 4", 114.00, "Disco externo 4TB para PlayStation 4", 10,
                    "https://fakestoreapi.com/img/61mtL65D4cL._AC_SX679_t.png",
                    electronica));
                productoService.guardar(new Producto(null,
                    "Monitor Full HD 21.5\" IPS", 599.00, "Monitor Full HD 21.5\" IPS", 7,
                    "https://fakestoreapi.com/img/81QpkIctqPL._AC_SX679_t.png",
                    electronica));
                productoService.guardar(new Producto(null,
                    "Monitor curvo gaming 49\" 144Hz QLED", 999.99, "Monitor curvo gaming 49\" 144Hz QLED", 4,
                    "https://fakestoreapi.com/img/81Zt42ioCgL._AC_SX679_t.png",
                    electronica));

                // Ropa femenina
                productoService.guardar(new Producto(null,
                    "Campera 3 en 1 para nieve", 56.99, "Campera 3 en 1 para nieve", 20,
                    "https://fakestoreapi.com/img/51Y5NI-I5jL._AC_UX679_t.png",
                    ropaFem));
                productoService.guardar(new Producto(null,
                    "Campera moto cuero sintético con capucha", 29.95, "Campera moto cuero sintético con capucha", 35,
                    "https://fakestoreapi.com/img/81XH0e8fefL._AC_UY879_t.png",
                    ropaFem));
                productoService.guardar(new Producto(null,
                    "Campera impermeable con capucha", 39.99, "Campera impermeable con capucha", 28,
                    "https://fakestoreapi.com/img/71HblAHs5xL._AC_UY879_-2t.png",
                    ropaFem));
                productoService.guardar(new Producto(null,
                    "Remera manga corta escote bote", 9.85, "Remera manga corta escote bote", 45,
                    "https://fakestoreapi.com/img/71z3kpMAYsL._AC_UY879_t.png",
                    ropaFem));
                productoService.guardar(new Producto(null,
                    "Remera deportiva manga corta", 7.95, "Remera deportiva manga corta", 50,
                    "https://fakestoreapi.com/img/51eg55uWmdL._AC_UX679_t.png",
                    ropaFem));
                productoService.guardar(new Producto(null,
                    "Remera casual de algodón manga corta", 12.99, "Remera casual de algodón manga corta",  38,
                    "https://fakestoreapi.com/img/61pHAEJ4NML._AC_UX679_t.png",
                    ropaFem));
            }
        };
    }
}
