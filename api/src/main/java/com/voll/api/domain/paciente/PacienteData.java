package com.voll.api.domain.paciente;

import com.voll.api.domain.direccion.DireccionData;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record PacienteData(
        @NotBlank
        String nombre,

        @NotBlank @Email
        String email,

        @NotBlank
        @Pattern(regexp = "\\d{10}")
        String telefono,

        @NotBlank
        @Pattern(regexp = "\\d{8,10}")
        String documento,

        @NotNull
        @Valid
        DireccionData direccion
) {
}
