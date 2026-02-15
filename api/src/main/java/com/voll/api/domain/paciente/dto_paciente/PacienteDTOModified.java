package com.voll.api.domain.paciente.dto_paciente;

import com.voll.api.domain.direccion.DireccionData;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PacienteDTOModified(
        @NotNull
        @Positive
        Long id,
        String nombre,
        String telefono,
        DireccionData direccion
) {
}
