package com.festival.modelo;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
public class Evento {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 500)
    private String descripcion;

    @Column(name = "fecha_realizacion", nullable = false)
    private LocalDateTime fechaRealizacion;

    @Column(nullable = false, length = 100)
    private String lugar;

    protected Evento() { }
    public Evento(String nombre, String descripcion, LocalDateTime fechaRealizacion, String lugar) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fechaRealizacion = fechaRealizacion;
        this.lugar = lugar;
    }
    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public LocalDateTime getFechaRealizacion() { return fechaRealizacion; }
    public void setFechaRealizacion(LocalDateTime fechaRealizacion) { this.fechaRealizacion = fechaRealizacion; }
    public String getLugar() { return lugar; }
    public void setLugar(String lugar) { this.lugar = lugar; }
}
