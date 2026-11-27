package com.festival.dto;

import java.time.LocalDateTime;
import jakarta.validation.constraints.*;

public record EventoSolicitud(
        @NotBlank @Size(max = 100) String nombre,
        @NotBlank @Size(max = 500) String descripcion,
        @NotNull LocalDateTime fechaRealizacion,
        @NotBlank @Size(max = 100) String lugar) { }
