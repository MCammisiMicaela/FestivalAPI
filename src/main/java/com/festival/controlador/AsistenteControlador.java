package com.festival.controlador;

import com.festival.dto.*;
import com.festival.servicio.AsistenteServicio;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/asistentes")
public class AsistenteControlador {
    private final AsistenteServicio servicio;
    public AsistenteControlador(AsistenteServicio servicio) { this.servicio = servicio; }

    @GetMapping
    public List<AsistenteRespuesta> listar() { return servicio.listar(); }

    @GetMapping("/{id}")
    public AsistenteRespuesta buscar(@PathVariable Long id) { return servicio.buscar(id); }

    @PostMapping
    public ResponseEntity<AsistenteRespuesta> crear(@Valid @RequestBody AsistenteSolicitud datos) {
        AsistenteRespuesta creado = servicio.crear(datos);
        return ResponseEntity.created(URI.create("/api/asistentes/" + creado.id())).body(creado);
    }

    @PutMapping("/{id}")
    public AsistenteRespuesta actualizar(@PathVariable Long id, @Valid @RequestBody AsistenteSolicitud datos) {
        return servicio.actualizar(id, datos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        servicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
