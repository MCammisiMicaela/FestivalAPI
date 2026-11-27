package com.festival.modelo;

import jakarta.persistence.*;

@Entity
public class Organizador extends Persona {
    @Column(nullable = false, length = 80)
    private String empresa;

    @Column(nullable = false, unique = true, length = 11)
    private String cuit;

    protected Organizador() { }
    public Organizador(String nombre, String apellido, String documento, String empresa, String cuit) {
        super(nombre, apellido, documento);
        this.empresa = empresa;
        this.cuit = cuit;
    }
    public String getEmpresa() { return empresa; }
    public void setEmpresa(String empresa) { this.empresa = empresa; }
    public String getCuit() { return cuit; }
    public void setCuit(String cuit) { this.cuit = cuit; }
}
