package com.festival.dto;

import java.math.BigDecimal;
import jakarta.validation.constraints.*;

public record EntradaSolicitud(
        @NotNull TipoEntrada tipo,
        @NotNull @Positive Long asistenteId,
        @NotNull @Positive Long organizadorId,
        @NotNull @Positive Long eventoId,
        @NotNull @DecimalMin(value = "0.01") BigDecimal precio,
        @NotBlank @Size(max = 40) String codigo) { }
