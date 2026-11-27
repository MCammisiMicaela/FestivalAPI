package com.festival.servicio;

import com.festival.dto.*;
import com.festival.error.*;
import com.festival.modelo.*;
import com.festival.repositorio.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EntradaServicioTest {
    @Mock EntradaRepo entradas;
    @Mock GeneralRepo generales;
    @Mock VipRepo vips;
    @Mock QRRepo qrs;
    @Mock AsistenteServicio asistentes;
    @Mock OrganizadorServicio organizadores;
    @Mock EventoServicio eventos;
    @InjectMocks EntradaServicio servicio;

    private Asistente asistente() { return new Asistente("Ana", "Prueba", "12345678", "123456"); }
    private Organizador organizador() { return new Organizador("Juan", "Prueba", "87654321", "Demo", "20999999999"); }
    private Evento evento() { return new Evento("Festival", "Evento de prueba", LocalDateTime.now().plusDays(1), "Sala 1"); }

    @Test
    void creaEntradaGeneralConRelaciones() {
        when(asistentes.entidad(1L)).thenReturn(asistente());
        when(organizadores.entidad(2L)).thenReturn(organizador());
        when(eventos.entidad(3L)).thenReturn(evento());
        when(entradas.save(any(Entrada.class))).thenAnswer(inv -> inv.getArgument(0));

        EntradaRespuesta resultado = servicio.crear(new EntradaSolicitud(
            TipoEntrada.GENERAL, 1L, 2L, 3L, new BigDecimal("15000.00"), "GENERAL-001"));

        assertEquals(TipoEntrada.GENERAL, resultado.tipo());
        assertEquals("GENERAL-001", resultado.codigo());
        assertEquals(new BigDecimal("15000.00"), resultado.precio());
        assertNotNull(resultado.fechaCompra());
        verify(entradas).save(any(General.class));
    }

    @Test
    void noPermiteCodigosRepetidosDelMismoTipo() {
        when(generales.existsByCodigoAlfanumerico("GENERAL-001")).thenReturn(true);

        assertThrows(Conflicto.class, () -> servicio.crear(new EntradaSolicitud(
            TipoEntrada.GENERAL, 1L, 2L, 3L, new BigDecimal("100"), "GENERAL-001")));
        verify(entradas, never()).save(any());
    }

    @Test
    void devuelve404SiNoExisteEntrada() {
        when(entradas.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RecursoNoEncontrado.class, () -> servicio.buscar(99L));
    }

    @Test
    void registraElPrimerUsoDeUnaEntradaQr() {
        QR qr = new QR(new BigDecimal("1200"), asistente(), organizador(), evento(), "QR-DEMO");
        when(entradas.findById(10L)).thenReturn(Optional.of(qr));
        when(entradas.save(qr)).thenReturn(qr);

        EntradaRespuesta respuesta = servicio.registrarUsoQr(10L);
        assertEquals(TipoEntrada.QR, respuesta.tipo());
        assertNotNull(respuesta.fechaUso());
    }

    @Test
    void noPermiteUtilizarDosVecesLaMismaEntradaQr() {
        QR qr = new QR(new BigDecimal("1200"), asistente(), organizador(), evento(), "QR-DEMO");
        qr.registrarUso(LocalDateTime.now());
        when(entradas.findById(10L)).thenReturn(Optional.of(qr));

        assertThrows(Conflicto.class, () -> servicio.registrarUsoQr(10L));
        verify(entradas, never()).save(any());
    }

    @Test
    void noPermiteRegistrarUsoEnUnaEntradaGeneral() {
        General general = new General(new BigDecimal("1000"), asistente(), organizador(), evento(), "GEN-DEMO");
        when(entradas.findById(11L)).thenReturn(Optional.of(general));
        assertThrows(Conflicto.class, () -> servicio.registrarUsoQr(11L));
    }
}
