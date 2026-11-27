package com.festival.dto;

import java.time.LocalDateTime;
import com.festival.modelo.Evento;

public record EventoRespuesta(Long id, String nombre, String descripcion, LocalDateTime fechaRealizacion, String lugar) {
    public static EventoRespuesta desde(Evento e) {
        return new EventoRespuesta(e.getId(), e.getNombre(), e.getDescripcion(), e.getFechaRealizacion(), e.getLugar());
    }
}
