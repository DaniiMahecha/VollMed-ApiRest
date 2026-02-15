package com.voll.api.domain.direccion;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record DireccionData(
        @NotBlank
        String calle,

        String numero, // ---> No es obligatorio
        String complemento, // ---> No es obligatorio

        @NotBlank
        String barrio,

        @JsonProperty("codigo_postal") //Así podemos rastrear la info
        @NotBlank @Pattern(regexp = "\\d{6}")
        String codigoPostal,

        @NotBlank
        String ciudad,
        @NotBlank
        String estado
) {}
