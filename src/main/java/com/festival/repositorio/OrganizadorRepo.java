 package com.festival.repositorio;

 import com.festival.modelo.Organizador;
 import org.springframework.data.jpa.repository.JpaRepository;
 import java.util.Optional;

 public interface OrganizadorRepo extends JpaRepository<Organizador, Long> {
     Optional<Organizador> findByCuit(String cuit);
}
