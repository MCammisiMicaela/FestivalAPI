package com.festival.repositorio;

import com.festival.modelo.Evento;
import org.springframework.data.jpa.repository.JpaRepository;


public interface EventoRepo extends JpaRepository<Evento, Long> {
}
