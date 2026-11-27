package com.festival.dto;

import java.math.BigDecimal;
import jakarta.validation.constraints.*;

public record EntradaActualizacion(@NotNull @DecimalMin("0.01") BigDecimal precio) { }
