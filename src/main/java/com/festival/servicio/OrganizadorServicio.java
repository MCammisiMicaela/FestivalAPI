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
public class OrganizadorServicio {
    private final OrganizadorRepo organizadores;
    private final PersonaRepo personas;
    private final EntradaRepo entradas;

    public OrganizadorServicio(OrganizadorRepo organizadores, PersonaRepo personas, EntradaRepo entradas) {
        this.organizadores = organizadores; this.personas = personas; this.entradas = entradas;
    }

    @Transactional(readOnly = true)
    public List<OrganizadorRespuesta> listar() {
        return organizadores.findAll(Sort.by("id")).stream().map(OrganizadorRespuesta::desde).toList();
    }
    @Transactional(readOnly = true)
    public OrganizadorRespuesta buscar(Long id) { return OrganizadorRespuesta.desde(entidad(id)); }
    @Transactional(readOnly = true)
    public OrganizadorRespuesta buscarCuit(String cuit) {
        return OrganizadorRespuesta.desde(organizadores.findByCuit(cuit)
                .orElseThrow(() -> new RecursoNoEncontrado("Organizador no encontrado para el CUIT indicado")));
    }
    @Transactional(readOnly = true)
    public Organizador entidad(Long id) {
        return organizadores.findById(id).orElseThrow(() -> new RecursoNoEncontrado("Organizador no encontrado: " + id));
    }

    private void validarUnicidad(String documento, String cuit, Long idActual) {
        personas.findByDocumento(documento).ifPresent(p -> {
            if (!p.getId().equals(idActual)) throw new Conflicto("Ya existe una persona con ese documento");
        });
        organizadores.findByCuit(cuit).ifPresent(o -> {
            if (!o.getId().equals(idActual)) throw new Conflicto("Ya existe un organizador con ese CUIT");
        });
    }

    @Transactional
    public OrganizadorRespuesta crear(OrganizadorSolicitud s) {
        validarUnicidad(s.documento(), s.cuit(), null);
        return OrganizadorRespuesta.desde(organizadores.save(new Organizador(s.nombre(), s.apellido(), s.documento(), s.empresa(), s.cuit())));
    }
    @Transactional
    public OrganizadorRespuesta actualizar(Long id, OrganizadorSolicitud s) {
        Organizador o = entidad(id);
        validarUnicidad(s.documento(), s.cuit(), id);
        o.setNombre(s.nombre()); o.setApellido(s.apellido());
        o.setDocumento(s.documento()); o.setEmpresa(s.empresa()); o.setCuit(s.cuit());
        return OrganizadorRespuesta.desde(organizadores.save(o));
    }
    @Transactional
    public void eliminar(Long id) {
        Organizador o = entidad(id);
        if (entradas.existsByOrganizador_Id(id)) throw new Conflicto("No se puede eliminar un organizador con entradas");
        organizadores.delete(o);
    }
}
