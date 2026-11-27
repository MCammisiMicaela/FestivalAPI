package com.festival.modelo;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Persona {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 60)
    private String nombre;

    @Column(nullable = false, length = 60)
    private String apellido;

    @Column(nullable = false, unique = true, length = 20)
    private String documento;

    protected Persona() { }
    protected Persona(String nombre, String apellido, String documento) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.documento = documento;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public String getDocumento() { return documento; }
    public void setDocumento(String documento) { this.documento = documento; }
}
