 package com.festival.repositorio;

 import com.festival.modelo.Vip;
 import org.springframework.data.jpa.repository.JpaRepository;


 public interface VipRepo extends JpaRepository<Vip, Long> {
     boolean existsByCodigoTarjeta(String codigo);
}
