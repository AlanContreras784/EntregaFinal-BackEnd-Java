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

                Categoria bolsasTextiles = categoriaService.guardar(
                        new Categoria(null, "Bolsas y textiles reutilizables",
                                "Bolsas de tela y textiles para reemplazar el plástico descartable"));
                Categoria ceroHuella = categoriaService.guardar(
                        new Categoria(null, "Cero Huella - Vajilla reutilizable",
                                "Vasos, tazas y botellas reutilizables para reducir residuos"));
                Categoria huertaJardin = categoriaService.guardar(
                        new Categoria(null, "Huerta y jardín",
                                "Kits y accesorios para cultivar en casa de forma sustentable"));
                Categoria papeleria = categoriaService.guardar(
                        new Categoria(null, "Papelería sustentable",
                                "Artículos de librería fabricados con materiales reciclados o plantables"));
                Categoria energiaHogar = categoriaService.guardar(
                        new Categoria(null, "Energía e iluminación",
                                "Luminarias solares y de bajo consumo para el hogar"));

                // Bolsas y textiles reutilizables
                productoService.guardar(new Producto(null,
                        "Bolsa reutilizable de tela modelo clásico", 9.99, "Bolsa reutilizable de tela modelo clásico",
                        40,
                        "https://i.postimg.cc/crY80KCz/bolsa-Reutilizable.jpg",
                        bolsasTextiles));
                productoService.guardar(new Producto(null,
                        "Bolsa reutilizable de tela modelo mediano", 10.99, "Bolsa reutilizable de tela modelo mediano",
                        35,
                        "https://i.postimg.cc/xX2NC0b1/bolsa-Reutilizable-2.jpg",
                        bolsasTextiles));
                productoService.guardar(new Producto(null,
                        "Bolsa reutilizable de tela modelo grande", 12.50, "Bolsa reutilizable de tela modelo grande",
                        30,
                        "https://i.postimg.cc/8sxr2dpW/bolsa-Reutilizable-3.jpg",
                        bolsasTextiles));
                productoService.guardar(new Producto(null,
                        "Bolsas Rock & Vida edición especial", 13.99, "Bolsas Rock & Vida edición especial", 20,
                        "https://i.postimg.cc/NyWcs05N/bolsas-Rock-amp-vida.jpg",
                        bolsasTextiles));

                // Cero Huella - Vajilla reutilizable
                productoService.guardar(new Producto(null,
                        "Botella Cero Huella acero inoxidable", 18.99, "Botella Cero Huella acero inoxidable", 25,
                        "https://i.postimg.cc/qNxh698X/botella-Cero-Huella.jpg",
                        ceroHuella));
                productoService.guardar(new Producto(null,
                        "Set de botellas reutilizables", 22.50, "Set de botellas reutilizables", 18,
                        "https://i.postimg.cc/zHh3zMzh/botellas-Reutilizables.jpg",
                        ceroHuella));
                productoService.guardar(new Producto(null,
                        "Taza Cero Huella", 11.99, "Taza Cero Huella", 30,
                        "https://i.postimg.cc/fkSVf3cb/taza-Cero-Huella-2.jpg",
                        ceroHuella));
                productoService.guardar(new Producto(null,
                        "Vaso Cero Huella 300cc", 7.99, "Vaso Cero Huella 300cc", 45,
                        "https://i.postimg.cc/7fgbjpQ8/vaso-Cerohuella-De300cc.jpg",
                        ceroHuella));
                productoService.guardar(new Producto(null,
                        "Vaso Cero Huella 500cc", 9.50, "Vaso Cero Huella 500cc", 40,
                        "https://i.postimg.cc/hJGjzh4c/vaso-Cero-Huella-De500cc.jpg",
                        ceroHuella));
                productoService.guardar(new Producto(null,
                        "Kit de cubiertos reutilizables para llevar", 8.75,
                        "Kit de cubiertos reutilizables para llevar", 33,
                        "https://i.postimg.cc/NyP01YLT/kit-De-Cubiertos.jpg",
                        ceroHuella));

                // Huerta y jardín
                productoService.guardar(new Producto(null,
                        "Kit de siembra para huerta en casa", 15.99, "Kit de siembra para huerta en casa", 22,
                        "https://i.postimg.cc/mzzgjqFn/kit-De-Siembra.jpg",
                        huertaJardin));
                productoService.guardar(new Producto(null,
                        "Pico auto-regable para macetas", 6.99, "Pico auto-regable para macetas", 28,
                        "https://i.postimg.cc/nC8CRH2G/pico-Auto-Regable2.jpg",
                        huertaJardin));
                productoService.guardar(new Producto(null,
                        "Cesto dual para separación de residuos", 24.99, "Cesto dual para separación de residuos", 15,
                        "https://i.postimg.cc/N955VFZp/cesto-Dual.jpg",
                        huertaJardin));

                // Papelería sustentable
                productoService.guardar(new Producto(null,
                        "Anotadores de papel reciclado", 4.50, "Anotadores de papel reciclado", 50,
                        "https://i.postimg.cc/bsjw0mY6/anotadores.jpg",
                        papeleria));
                productoService.guardar(new Producto(null,
                        "Lapicera ecológica de material reciclado", 3.20, "Lapicera ecológica de material reciclado",
                        60,
                        "https://i.postimg.cc/zbsXcjCG/lapicera.jpg",
                        papeleria));
                productoService.guardar(new Producto(null,
                        "Lápices de colores de madera certificada", 5.99, "Lápices de colores de madera certificada",
                        45,
                        "https://i.postimg.cc/RNQVgwNv/lapices-Colores.png",
                        papeleria));
                productoService.guardar(new Producto(null,
                        "Lápiz plantable (germina al enterrarlo)", 2.99, "Lápiz plantable (germina al enterrarlo)", 70,
                        "https://i.postimg.cc/Cd8Kf30B/lapiz-Plantable.jpg",
                        papeleria));

                // Energía e iluminación
                productoService.guardar(new Producto(null,
                        "Luminaria solar de tres lámparas", 34.99, "Luminaria solar de tres lámparas", 12,
                        "https://i.postimg.cc/jW8DZP3D/luminaria-Tres-Lamparas.jpg",
                        energiaHogar));
                productoService.guardar(new Producto(null,
                        "Luminaria solar de una lámpara", 19.99, "Luminaria solar de una lámpara", 20,
                        "https://i.postimg.cc/mzJtGRsR/luminaria-Una-Lampara.jpg",
                        energiaHogar));
                productoService.guardar(new Producto(null,
                        "Velador de escritorio bajo consumo", 16.50, "Velador de escritorio bajo consumo", 18,
                        "https://i.postimg.cc/Xr4XWQTZ/velador-Escritorio.jpg",
                        energiaHogar));
            }
        };
    }
}