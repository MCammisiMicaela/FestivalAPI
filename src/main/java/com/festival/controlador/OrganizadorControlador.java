package com.festival.controlador;

import com.festival.dto.*;
import com.festival.servicio.OrganizadorServicio;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/organizadores")
public class OrganizadorControlador {
    private final OrganizadorServicio servicio;
    public OrganizadorControlador(OrganizadorServicio servicio) { this.servicio = servicio; }

    @GetMapping
    public List<OrganizadorRespuesta> listar() { return servicio.listar(); }

    @GetMapping("/{id}")
    public OrganizadorRespuesta buscar(@PathVariable Long id) { return servicio.buscar(id); }

   @GetMapping("/cuit/{cuit}")
   public OrganizadorRespuesta porCuit(@PathVariable String cuit) { return servicio.buscarCuit(cuit); }

    @PostMapping
    public ResponseEntity<OrganizadorRespuesta> crear(@Valid @RequestBody OrganizadorSolicitud datos) {
        OrganizadorRespuesta creado = servicio.crear(datos);
        return ResponseEntity.created(URI.create("/api/organizadores/" + creado.id())).body(creado);
    }

    @PutMapping("/{id}")
    public OrganizadorRespuesta actualizar(@PathVariable Long id, @Valid @RequestBody OrganizadorSolicitud datos) {
        return servicio.actualizar(id, datos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        servicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
