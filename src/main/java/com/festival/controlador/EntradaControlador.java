package com.festival.controlador;

import com.festival.dto.*;
import com.festival.servicio.EntradaServicio;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/entradas")
public class EntradaControlador {
    private final EntradaServicio servicio;
    public EntradaControlador(EntradaServicio servicio) { this.servicio = servicio; }

    @GetMapping
    public List<EntradaRespuesta> listar() { return servicio.listar(); }

    @GetMapping("/{id}")
    public EntradaRespuesta buscar(@PathVariable Long id) { return servicio.buscar(id); }

    @PostMapping
    public ResponseEntity<EntradaRespuesta> crear(@Valid @RequestBody EntradaSolicitud datos) {
        EntradaRespuesta creada = servicio.crear(datos);
        return ResponseEntity.created(URI.create("/api/entradas/" + creada.id())).body(creada);
    }

    @PatchMapping("/{id}/precio")
    public EntradaRespuesta actualizarPrecio(@PathVariable Long id, @Valid @RequestBody EntradaActualizacion datos) {
        return servicio.actualizarPrecio(id, datos);
    }

    @PatchMapping("/{id}/usar")
    public EntradaRespuesta registrarUso(@PathVariable Long id) { return servicio.registrarUsoQr(id); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        servicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
