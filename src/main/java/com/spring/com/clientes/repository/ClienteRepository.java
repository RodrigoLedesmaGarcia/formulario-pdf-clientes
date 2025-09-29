package com.spring.com.clientes.repository;

import com.spring.com.clientes.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Optional<Cliente> findClienteByCorreoIgnoreCase(String correo);

    @Query("""
          SELECT c FROM Cliente c 
          WHERE (:nombre IS NULL OR LOWER(c.nombre) LIKE LOWER (CONCAT('%', :nombre, '%')))
          AND (:apellidoPaterno IS NULL OR LOWER(c.app) LIKE LOWER (CONCAT('%', :app, '%')))
          AND (:apellidoMaterno IS NULL OR LOWER(c.apm) LIKE LOWER (CONCAT('%', :apm, '%')))                              
          """)
    List<Cliente> findUsuarioByFullName
            (@Param("nombre") String nombre, @Param("app") String app, @Param("apm") String apm);
}
