package com.festival.dto;

import jakarta.validation.constraints.*;

public record OrganizadorSolicitud(
        @NotBlank @Size(max = 60) String nombre,
        @NotBlank @Size(max = 60) String apellido,
        @NotBlank @Pattern(regexp = "[0-9]{7,11}", message = "Debe contener entre 7 y 11 digitos") String documento,
        @NotBlank @Size(max = 80) String empresa,
        @NotBlank @Pattern(regexp = "[0-9]{11}", message = "El CUIT debe contener 11 digitos") String cuit) { }
