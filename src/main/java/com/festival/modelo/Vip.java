package com.festival.modelo;

import java.math.BigDecimal;
import jakarta.persistence.*;

@Entity
public class Vip extends Entrada {
    @Column(nullable = false, unique = true, length = 40)
    private String codigoTarjeta;
    protected Vip() { }
    public Vip(BigDecimal precio, Asistente asistente, Organizador organizador, Evento evento, String codigo) {
        super(precio, asistente, organizador, evento);
        this.codigoTarjeta = codigo;
    }
    public String getCodigoTarjeta() { return codigoTarjeta; }
}
