package com.festival.servicio;

import com.festival.dto.AsistenteSolicitud;
import com.festival.error.Conflicto;
import com.festival.modelo.Asistente;
import com.festival.repositorio.*;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AsistenteServicioTest {
    @Mock AsistenteRepo asistentes;
    @Mock PersonaRepo personas;
    @Mock EntradaRepo entradas;
    @InjectMocks AsistenteServicio servicio;

    @Test
    void evitaDuplicarDocumento() {
        when(personas.findByDocumento("12345678"))
                .thenReturn(Optional.of(new Asistente("Ana", "Demo", "12345678", "111111")));
        assertThrows(Conflicto.class, () -> servicio.crear(
            new AsistenteSolicitud("Otra", "Persona", "12345678", "222222")));
        verifyNoInteractions(asistentes);
    }
}
