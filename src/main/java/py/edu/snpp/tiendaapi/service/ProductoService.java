package py.edu.snpp.tiendaapi.service;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import py.edu.snpp.tiendaapi.model.Producto;
import py.edu.snpp.tiendaapi.repository.ProductoRepository;



@Service
public class ProductoService {

    // Ya no lleva 'final' si lo inyectas por constructor manual o por @Autowired
    private final ProductoRepository productoRepository;

    // Constructor manual (Spring Boot lo detecta automáticamente)
    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }


    // Listar todos los productos
    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    // Buscar producto por ID
    public Producto buscarPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Producto no encontrado: " + id));
    }

    // Buscar por nombre
    public List<Producto> buscarPorNombre(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre);
    }

    // Registrar producto
    public Producto registrarProducto(Producto producto) {
        producto.setId(null);
        return productoRepository.save(producto);
    }

    // Actualizar producto
    public Producto actualizarProducto(Long id, Producto datos) {

        Producto existente = buscarPorId(id);

        existente.setNombre(datos.getNombre());
        existente.setDescripcion(datos.getDescripcion());
        existente.setPrecio(datos.getPrecio());
        existente.setStock(datos.getStock());

        return productoRepository.save(existente);
    }

    // Eliminar producto
    public void eliminarProducto(Long id) {
        Producto existente = buscarPorId(id);
        productoRepository.delete(existente);
    }
}
