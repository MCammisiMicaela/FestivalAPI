 package com.festival.repositorio;

 import com.festival.modelo.General;
 import org.springframework.data.jpa.repository.JpaRepository;


 public interface GeneralRepo extends JpaRepository<General, Long> {
     boolean existsByCodigoAlfanumerico(String codigo);
}
