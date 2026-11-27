package com.festival.integracion;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Prueba controladores, servicios y persistencia SQLite en una misma ejecución.
 * Cada prueba se revierte para no compartir sus datos con las demás.
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:sqlite:festival-integracion-test.db",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@Transactional
class FestivalApiIntegracionTest {

    @Autowired
    private MockMvc mvc;

    private String crearYObtenerRuta(String ruta, String json) throws Exception {
        MvcResult resultado = mvc.perform(post(ruta)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andReturn();
        return resultado.getResponse().getHeader("Location");
    }

    private Long extraerId(String ruta) {
        return Long.parseLong(ruta.substring(ruta.lastIndexOf('/') + 1));
    }

    @Test
    void realizaAltaConsultaModificacionYEliminacionDeEvento() throws Exception {
        String ruta = crearYObtenerRuta("/api/eventos", """
                {"nombre":"Festival de Prueba", "descripcion":"Evento creado desde una prueba HTTP",
                 "fechaRealizacion":"2027-06-15T20:00:00", "lugar":"Anfiteatro"}
                """);

        mvc.perform(get(ruta))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Festival de Prueba"));

        mvc.perform(put(ruta).contentType(MediaType.APPLICATION_JSON).content("""
                {"nombre":"Festival Actualizado", "descripcion":"Evento corregido",
                 "fechaRealizacion":"2027-06-15T21:00:00", "lugar":"Parque"}
                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Festival Actualizado"));

        mvc.perform(delete(ruta)).andExpect(status().isNoContent());
        mvc.perform(get(ruta)).andExpect(status().isNotFound());
    }

    @Test
    void devuelveErroresHttpParaDatosInvalidosYRecursosInexistentes() throws Exception {
        mvc.perform(post("/api/eventos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"\",\"descripcion\":\"\",\"lugar\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.estado").value(400));

        mvc.perform(get("/api/eventos/999999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.estado").value(404));

        mvc.perform(post("/api/entradas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"tipo\":\"QR\",\"precio\":-10}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void emiteEntradaQrValidaReferenciasYEvitaUsarlaDosVeces() throws Exception {
        Long asistenteId = extraerId(crearYObtenerRuta("/api/asistentes", """
                {"nombre":"Ana","apellido":"Ejemplo","documento":"99999991","telefono":"0000000000"}
                """));
        Long organizadorId = extraerId(crearYObtenerRuta("/api/organizadores", """
                {"nombre":"Juan","apellido":"Ejemplo","documento":"99999992",
                 "empresa":"Organización de prueba","cuit":"20999999999"}
                """));
        Long eventoId = extraerId(crearYObtenerRuta("/api/eventos", """
                {"nombre":"Festival QR","descripcion":"Evento ficticio para pruebas",
                 "fechaRealizacion":"2027-06-15T20:00:00","lugar":"Anfiteatro"}
                """));

        String cuerpo = """
                {"tipo":"QR","asistenteId":%d,"organizadorId":%d,"eventoId":%d,
                 "precio":1200.00,"codigo":"QR-PRUEBA-001"}
                """.formatted(asistenteId, organizadorId, eventoId);

        String rutaEntrada = crearYObtenerRuta("/api/entradas", cuerpo);

        mvc.perform(get(rutaEntrada))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipo").value("QR"))
                .andExpect(jsonPath("$.eventoId").value(eventoId.intValue()));

        mvc.perform(post("/api/entradas")
                        .contentType(MediaType.APPLICATION_JSON).content(cuerpo))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.estado").value(409));

        mvc.perform(delete("/api/eventos/" + eventoId))
                .andExpect(status().isConflict());

        mvc.perform(patch(rutaEntrada + "/usar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fechaUso").isNotEmpty());

        mvc.perform(patch(rutaEntrada + "/usar"))
                .andExpect(status().isConflict());
    }
}
