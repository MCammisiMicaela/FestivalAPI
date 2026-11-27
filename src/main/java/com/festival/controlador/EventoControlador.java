package com.festival.controlador;

import com.festival.dto.*;
import com.festival.servicio.EventoServicio;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/eventos")
public class EventoControlador {
    private final EventoServicio servicio;
    public EventoControlador(EventoServicio servicio) { this.servicio = servicio; }

    @GetMapping
    public List<EventoRespuesta> listar() { return servicio.listar(); }

    @GetMapping("/{id}")
    public EventoRespuesta buscar(@PathVariable Long id) { return servicio.buscar(id); }

    @PostMapping
    public ResponseEntity<EventoRespuesta> crear(@Valid @RequestBody EventoSolicitud datos) {
        EventoRespuesta creado = servicio.crear(datos);
        return ResponseEntity.created(URI.create("/api/eventos/" + creado.id())).body(creado);
    }

    @PutMapping("/{id}")
    public EventoRespuesta actualizar(@PathVariable Long id, @Valid @RequestBody EventoSolicitud datos) {
        return servicio.actualizar(id, datos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        servicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
