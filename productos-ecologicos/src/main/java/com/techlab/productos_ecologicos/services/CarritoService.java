package com.techlab.productos_ecologicos.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.techlab.productos_ecologicos.dto.CarritoResumenDTO;
import com.techlab.productos_ecologicos.exception.CarritoNoEncontradoException;
import com.techlab.productos_ecologicos.exception.ProductoNoEncontradoEnElCarritoException;
import com.techlab.productos_ecologicos.exception.StockInsuficienteException;
import com.techlab.productos_ecologicos.models.Carrito;
import com.techlab.productos_ecologicos.models.CarritoProducto;
import com.techlab.productos_ecologicos.models.Producto;
import com.techlab.productos_ecologicos.repository.CarritoProductoRepository;
import com.techlab.productos_ecologicos.repository.CarritoRepository;

@Service
public class CarritoService {

    private final CarritoRepository carritoRepository;
    private final ProductoService productoService;
    private final CarritoProductoRepository carritoProductoRepository;

    public CarritoService(CarritoRepository carritoRepository, CarritoProductoRepository carritoProductoRepository, ProductoService productoService) {
        this.carritoRepository = carritoRepository;
        this.productoService = productoService;
        this.carritoProductoRepository = carritoProductoRepository;                 
    }

    public Carrito crear() {
        return carritoRepository.save(new Carrito());
    }

    // obtenerPorId centraliza la validación de existencia.
    // Todos los métodos que necesitan un carrito lo llaman primero.
    public Carrito obtenerPorId(Integer id) {
        return carritoRepository.findById(id)
                .orElseThrow(() -> new CarritoNoEncontradoException(
                        "No se encontró un carrito con id " + id));
    }

    public List<Carrito> listarTodos() {
        return carritoRepository.findAll();
    }

    public Carrito agregarProducto(Integer carritoId, Integer productoId) {
        Carrito carrito = obtenerPorId(carritoId);
        Producto producto = productoService.obtenerPorId(productoId);

        if (producto.getStock() <= 0) {
            throw new StockInsuficienteException(
                    "El producto \"" + producto.getNombre() + "\" no tiene stock disponible.");
        }

        // buscamos si ya existe una fila con este producto en el carrito
        Optional<CarritoProducto> existente = carritoProductoRepository.findByCarritoAndProducto(carrito, producto);

        if(existente.isPresent()){
            // El producto ya esta en el carrito incrementamos 
            CarritoProducto cp = existente.get();
            cp.setCantidad(cp.getCantidad() + 1);
            carritoProductoRepository.save(cp);

        }else{
            // el producto no esta en el carrito - creamos una fila nueva
            CarritoProducto nuevo = new CarritoProducto(null,carrito,producto,1);
            carritoProductoRepository.save(nuevo);
        }

        // Descuenta una unidad de stock y persiste el cambio
        producto.setStock(producto.getStock() - 1);
        productoService.guardar(producto);

        return carritoRepository.save(carrito);
    }


    // Descontar un producto del carrito. Si la cantidad es mayor a 1, solo disminuye la cantidad. Si es 1, elimina el registro.
    public Carrito descontarProducto(Integer carritoId, Integer productoId) {
        Carrito carrito = obtenerPorId(carritoId);
        Producto producto = productoService.obtenerPorId(productoId);
        // Buscar el producto dentro del carrito
        CarritoProducto carritoProducto = carritoProductoRepository
                .findByCarritoAndProducto(carrito, producto)
                .orElseThrow(() ->
                    new ProductoNoEncontradoEnElCarritoException("El producto no se encuentra en el carrito."));
        if (carritoProducto.getCantidad() > 1) {
            // Si hay más de una unidad, disminuye la cantidad
            carritoProducto.setCantidad(carritoProducto.getCantidad() - 1);
            carritoProductoRepository.save(carritoProducto);
        } else {
            // Si era la última unidad, elimina el registro
            carritoProductoRepository.delete(carritoProducto);
        }
        // Devuelve una unidad al stock
        producto.setStock(producto.getStock() + 1);
        productoService.guardar(producto);
        return carritoRepository.save(carrito);
    }

    // clear() quita los productos de la lista en memoria.
    // save() persiste ese cambio eliminando las filas de la tabla intermedia.
    // public Carrito vaciar(Integer id) {
    //     Carrito carrito = obtenerPorId(id);
    //     carrito.getProductos().clear();
    //     return carritoRepository.save(carrito);
    // }

    // Elimina un producto del carrito y devuelve la cantidad al stock
    public Carrito eliminarProducto(Integer carritoId, Integer productoId) {
    Carrito carrito = obtenerPorId(carritoId);
        Producto producto = productoService.obtenerPorId(productoId);
        CarritoProducto cp = carritoProductoRepository
                .findByCarritoAndProducto(carrito, producto)
                .orElseThrow(() -> new RuntimeException("Producto no está en el carrito"));
        // devolver al stock la cantidad que había en el carrito
        producto.setStock(producto.getStock() + cp.getCantidad());
        productoService.guardar(producto);
        // eliminar relación
        carritoProductoRepository.delete(cp);
        return carritoRepository.save(carrito);
    }

    // Vacía el carrito y devuelve el stock de todos los productos
    public Carrito vaciar(Integer carritoId) {
        Carrito carrito = obtenerPorId(carritoId);
        // Recorremos todos los productos del carrito
        for (CarritoProducto cp : carrito.getProductos()) {
            Producto producto = cp.getProducto();
            // Devuelve al stock la cantidad que había en el carrito
            producto.setStock(producto.getStock() + cp.getCantidad());
            productoService.guardar(producto);
        }
        // Elimina todas las relaciones carrito-producto
        carrito.getProductos().clear();
        return carritoRepository.save(carrito);
    }


    // Elimina el carrito y devuelve el stock de todos los productos
    public void eliminar(Integer id) {
        Carrito carrito = obtenerPorId(id);
        carritoRepository.delete(carrito);
    }

    // Calcula el subtotal, el envío y el total del carrito
    public CarritoResumenDTO obtenerResumen(Integer carritoId) {
        Carrito carrito = obtenerPorId(carritoId);
        int cantidad = 0;
        double subtotal = 0;

        for (CarritoProducto cp : carrito.getProductos()) {
            cantidad += cp.getCantidad();
            subtotal += cp.getCantidad() * cp.getProducto().getPrecio();
        }
        double envio = 0.0;
        double total = subtotal + envio;
        return new CarritoResumenDTO(
                cantidad,
                subtotal,
                envio,
                total
        );
    }
}
