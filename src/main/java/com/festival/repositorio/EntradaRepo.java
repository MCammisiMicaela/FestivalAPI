 package com.festival.repositorio;

 import com.festival.modelo.Entrada;
 import org.springframework.data.jpa.repository.JpaRepository;


 public interface EntradaRepo extends JpaRepository<Entrada, Long> {
     boolean existsByAsistente_Id(Long id);
    boolean existsByOrganizador_Id(Long id);
    boolean existsByEvento_Id(Long id);
}
