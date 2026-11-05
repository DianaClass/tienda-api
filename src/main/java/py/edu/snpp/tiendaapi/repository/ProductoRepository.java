package py.edu.snpp.tiendaapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import py.edu.snpp.tiendaapi.model.Producto;

public interface ProductoRepository
        extends JpaRepository<Producto, Long> {

    List<Producto> findByNombreContainingIgnoreCase(String nombre);
}
