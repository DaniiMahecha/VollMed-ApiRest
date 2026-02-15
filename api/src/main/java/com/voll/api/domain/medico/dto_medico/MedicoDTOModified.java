package com.voll.api.domain.medico.dto_medico;


import com.voll.api.domain.direccion.DireccionData;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record MedicoDTOModified(
        @NotNull
        @Positive
        Long id,
        String nombre,
        String telefono,
        DireccionData direccion
) {}
