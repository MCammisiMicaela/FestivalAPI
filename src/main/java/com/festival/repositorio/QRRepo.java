 package com.festival.repositorio;

 import com.festival.modelo.QR;
 import org.springframework.data.jpa.repository.JpaRepository;


 public interface QRRepo extends JpaRepository<QR, Long> {
     boolean existsByCodigoQr(String codigo);
}
