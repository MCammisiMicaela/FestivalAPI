package com.festival.modelo;

import java.math.BigDecimal;
import jakarta.persistence.*;

@Entity
public class General extends Entrada {
    @Column(nullable = false, unique = true, length = 40)
    private String codigoAlfanumerico;
    protected General() { }
    public General(BigDecimal precio, Asistente asistente, Organizador organizador, Evento evento, String codigo) {
        super(precio, asistente, organizador, evento);
        this.codigoAlfanumerico = codigo;
    }
    public String getCodigoAlfanumerico() { return codigoAlfanumerico; }
}
