package com.festival.dto;

import com.festival.modelo.Asistente;

public record AsistenteRespuesta(Long id, String nombre, String apellido, String documento, String telefono) {
    public static AsistenteRespuesta desde(Asistente a) {
        return new AsistenteRespuesta(a.getId(), a.getNombre(), a.getApellido(), a.getDocumento(), a.getTelefono());
    }
}
