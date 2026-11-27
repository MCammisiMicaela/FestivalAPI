package com.festival.dto;

import com.festival.modelo.Organizador;

public record OrganizadorRespuesta(Long id, String nombre, String apellido, String documento, String empresa, String cuit) {
    public static OrganizadorRespuesta desde(Organizador o) {
        return new OrganizadorRespuesta(o.getId(), o.getNombre(), o.getApellido(), o.getDocumento(), o.getEmpresa(), o.getCuit());
    }
}
