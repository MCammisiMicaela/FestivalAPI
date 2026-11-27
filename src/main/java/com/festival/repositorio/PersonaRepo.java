package com.festival.repositorio;

import com.festival.modelo.Persona;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonaRepo extends JpaRepository<Persona, Long> {
    Optional<Persona> findByDocumento(String documento);
}
