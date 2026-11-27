package com.festival.modelo;

import jakarta.persistence.*;

@Entity
public class Asistente extends Persona {
    @Column(nullable = false, length = 30)
    private String telefono;

    protected Asistente() { }
    public Asistente(String nombre, String apellido, String documento, String telefono) {
        super(nombre, apellido, documento);
        this.telefono = telefono;
    }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
}
