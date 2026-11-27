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
public class AsistenteServicio {
    private final AsistenteRepo asistentes;
    private final PersonaRepo personas;
    private final EntradaRepo entradas;

    public AsistenteServicio(AsistenteRepo asistentes, PersonaRepo personas, EntradaRepo entradas) {
        this.asistentes = asistentes;
        this.personas = personas;
        this.entradas = entradas;
    }

    @Transactional(readOnly = true)
    public List<AsistenteRespuesta> listar() {
        return asistentes.findAll(Sort.by("id")).stream().map(AsistenteRespuesta::desde).toList();
    }

    @Transactional(readOnly = true)
    public AsistenteRespuesta buscar(Long id) { return AsistenteRespuesta.desde(entidad(id)); }

    @Transactional(readOnly = true)
    public Asistente entidad(Long id) {
        return asistentes.findById(id).orElseThrow(() -> new RecursoNoEncontrado("Asistente no encontrado: " + id));
    }

    private void verificarDocumento(String documento, Long idActual) {
        personas.findByDocumento(documento).ifPresent(p -> {
            if (!p.getId().equals(idActual)) throw new Conflicto("Ya existe una persona con ese documento");
        });
    }

    @Transactional
    public AsistenteRespuesta crear(AsistenteSolicitud s) {
        verificarDocumento(s.documento(), null);
        return AsistenteRespuesta.desde(asistentes.save(new Asistente(s.nombre(), s.apellido(), s.documento(), s.telefono())));
    }

    @Transactional
    public AsistenteRespuesta actualizar(Long id, AsistenteSolicitud s) {
        Asistente a = entidad(id);
        verificarDocumento(s.documento(), id);
        a.setNombre(s.nombre()); a.setApellido(s.apellido());
        a.setDocumento(s.documento()); a.setTelefono(s.telefono());
        return AsistenteRespuesta.desde(asistentes.save(a));
    }

    @Transactional
    public void eliminar(Long id) {
        Asistente a = entidad(id);
        if (entradas.existsByAsistente_Id(id)) throw new Conflicto("No se puede eliminar un asistente con entradas");
        asistentes.delete(a);
    }
}
