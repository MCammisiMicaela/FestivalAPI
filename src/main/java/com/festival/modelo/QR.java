package com.festival.modelo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
public class QR extends Entrada {
    @Column(nullable = false, unique = true, length = 40)
    private String codigoQr;

    // Es nula hasta que la entrada sea utilizada.
    private LocalDateTime fechaUso;

    protected QR() { }
    public QR(BigDecimal precio, Asistente asistente, Organizador organizador, Evento evento, String codigo) {
        super(precio, asistente, organizador, evento);
        this.codigoQr = codigo;
    }
    public String getCodigoQr() { return codigoQr; }
    public LocalDateTime getFechaUso() { return fechaUso; }
    public void registrarUso(LocalDateTime momento) { this.fechaUso = momento; }
}
