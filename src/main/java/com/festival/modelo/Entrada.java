package com.festival.modelo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Entrada {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime fechaCompra;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal precio;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "asistente_id", nullable = false)
    private Asistente asistente;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "organizador_id", nullable = false)
    private Organizador organizador;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "evento_id", nullable = false)
    private Evento evento;

    protected Entrada() { }
    protected Entrada(BigDecimal precio, Asistente asistente, Organizador organizador, Evento evento) {
        this.fechaCompra = LocalDateTime.now();
        this.precio = precio;
        this.asistente = asistente;
        this.organizador = organizador;
        this.evento = evento;
    }
    public Long getId() { return id; }
    public LocalDateTime getFechaCompra() { return fechaCompra; }
    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }
    public Asistente getAsistente() { return asistente; }
    public Organizador getOrganizador() { return organizador; }
    public Evento getEvento() { return evento; }
}
