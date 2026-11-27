package com.festival.servicio;

import com.festival.dto.*;
import com.festival.error.*;
import com.festival.modelo.Evento;
import com.festival.repositorio.*;
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
class EventoServicioTest {
    @Mock EventoRepo eventos;
    @Mock EntradaRepo entradas;
    @InjectMocks EventoServicio servicio;

    @Test
    void creaUnEventoConLosDatosSolicitados() {
        when(eventos.save(any(Evento.class))).thenAnswer(inv -> inv.getArgument(0));
        EventoRespuesta creado = servicio.crear(new EventoSolicitud(
            "Festival Demo", "Festival de prueba", LocalDateTime.of(2027, 6, 15, 20, 0), "Anfiteatro"));
        assertEquals("Festival Demo", creado.nombre());
        assertEquals("Anfiteatro", creado.lugar());
    }

    @Test
    void rechazaBorrarUnEventoConEntradas() {
        when(eventos.findById(7L)).thenReturn(Optional.of(new Evento("Demo", "Prueba", LocalDateTime.now(), "Sala")));
        when(entradas.existsByEvento_Id(7L)).thenReturn(true);

        assertThrows(Conflicto.class, () -> servicio.eliminar(7L));
        verify(eventos, never()).delete(any());
    }

    @Test
    void devuelve404CuandoNoExisteElEvento() {
        when(eventos.findById(900L)).thenReturn(Optional.empty());
        assertThrows(RecursoNoEncontrado.class, () -> servicio.buscar(900L));
    }
}
