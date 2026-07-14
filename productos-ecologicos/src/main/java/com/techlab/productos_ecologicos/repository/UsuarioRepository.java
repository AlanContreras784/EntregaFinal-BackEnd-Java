package com.techlab.productos_ecologicos.repository;

import com.techlab.productos_ecologicos.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    // Spring Security necesita buscar el usuario por username para validar credenciales.
    // JPA genera la query automáticamente a partir del nombre del método.
    Optional<Usuario> findByUsername(String username);
}
