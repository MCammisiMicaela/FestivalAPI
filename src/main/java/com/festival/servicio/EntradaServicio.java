package com.festival.servicio;

import com.festival.dto.*;
import com.festival.error.*;
import com.festival.modelo.*;
import com.festival.repositorio.*;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EntradaServicio {
    private final EntradaRepo entradas;
    private final GeneralRepo generales;
    private final VipRepo vips;
    private final QRRepo qrs;
    private final AsistenteServicio asistentes;
    private final OrganizadorServicio organizadores;
    private final EventoServicio eventos;

    public EntradaServicio(EntradaRepo entradas, GeneralRepo generales, VipRepo vips, QRRepo qrs,
                           AsistenteServicio asistentes, OrganizadorServicio organizadores, EventoServicio eventos) {
        this.entradas = entradas; this.generales = generales; this.vips = vips; this.qrs = qrs;
        this.asistentes = asistentes; this.organizadores = organizadores; this.eventos = eventos;
    }

    @Transactional(readOnly = true)
    public List<EntradaRespuesta> listar() {
        return entradas.findAll(Sort.by("id")).stream().map(EntradaRespuesta::desde).toList();
    }

    @Transactional(readOnly = true)
    public EntradaRespuesta buscar(Long id) { return EntradaRespuesta.desde(entidad(id)); }

    private Entrada entidad(Long id) {
        return entradas.findById(id).orElseThrow(() -> new RecursoNoEncontrado("Entrada no encontrada: " + id));
    }

    @Transactional
    public EntradaRespuesta crear(EntradaSolicitud s) {
        if (s.tipo() == null) throw new IllegalArgumentException("El tipo es obligatorio");
        validarCodigo(s.tipo(), s.codigo());
        Asistente asistente = asistentes.entidad(s.asistenteId());
        Organizador organizador = organizadores.entidad(s.organizadorId());
        Evento evento = eventos.entidad(s.eventoId());
        Entrada entrada = switch (s.tipo()) {
            case GENERAL -> new General(s.precio(), asistente, organizador, evento, s.codigo());
            case VIP -> new Vip(s.precio(), asistente, organizador, evento, s.codigo());
            case QR -> new QR(s.precio(), asistente, organizador, evento, s.codigo());
        };
        return EntradaRespuesta.desde(entradas.save(entrada));
    }

    private void validarCodigo(TipoEntrada tipo, String codigo) {
        boolean existe = switch (tipo) {
            case GENERAL -> generales.existsByCodigoAlfanumerico(codigo);
            case VIP -> vips.existsByCodigoTarjeta(codigo);
            case QR -> qrs.existsByCodigoQr(codigo);
        };
        if (existe) throw new Conflicto("Ya existe una entrada de ese tipo con el mismo codigo");
    }

    @Transactional
    public EntradaRespuesta actualizarPrecio(Long id, EntradaActualizacion s) {
        Entrada entrada = entidad(id);
        entrada.setPrecio(s.precio());
        return EntradaRespuesta.desde(entradas.save(entrada));
    }

    @Transactional
    public EntradaRespuesta registrarUsoQr(Long id) {
        Entrada entrada = entidad(id);
        if (!(entrada instanceof QR qr)) throw new Conflicto("Solo las entradas QR se pueden registrar como utilizadas");
        if (qr.getFechaUso() != null) throw new Conflicto("La entrada QR ya fue utilizada");
        qr.registrarUso(LocalDateTime.now());
        return EntradaRespuesta.desde(entradas.save(qr));
    }

    @Transactional
    public void eliminar(Long id) { entradas.delete(entidad(id)); }
}
