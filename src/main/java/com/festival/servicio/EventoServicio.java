package com.festival.servicio;

import com.festival.dto.*;
import com.festival.error.*;
import com.festival.modelo.*;
import com.festival.repositorio.*;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EventoServicio {
    private final EventoRepo eventos;
    private final EntradaRepo entradas;

    public EventoServicio(EventoRepo eventos, EntradaRepo entradas) {
        this.eventos = eventos; this.entradas = entradas;
    }
    @Transactional(readOnly = true)
    public List<EventoRespuesta> listar() {
        return eventos.findAll(Sort.by("id")).stream().map(EventoRespuesta::desde).toList();
    }
    @Transactional(readOnly = true)
    public EventoRespuesta buscar(Long id) { return EventoRespuesta.desde(entidad(id)); }
    @Transactional(readOnly = true)
    public Evento entidad(Long id) {
        return eventos.findById(id).orElseThrow(() -> new RecursoNoEncontrado("Evento no encontrado: " + id));
    }
    @Transactional
    public EventoRespuesta crear(EventoSolicitud s) {
        return EventoRespuesta.desde(eventos.save(new Evento(s.nombre(), s.descripcion(), s.fechaRealizacion(), s.lugar())));
    }
    @Transactional
    public EventoRespuesta actualizar(Long id, EventoSolicitud s) {
        Evento e = entidad(id);
        e.setNombre(s.nombre()); e.setDescripcion(s.descripcion());
        e.setFechaRealizacion(s.fechaRealizacion()); e.setLugar(s.lugar());
        return EventoRespuesta.desde(eventos.save(e));
    }
    @Transactional
    public void eliminar(Long id) {
        Evento e = entidad(id);
        if (entradas.existsByEvento_Id(id)) throw new Conflicto("No se puede eliminar un evento que tiene entradas");
        eventos.delete(e);
    }
}
