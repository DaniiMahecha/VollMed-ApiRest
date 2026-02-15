package com.voll.api.domain.paciente.dto_paciente;

import com.voll.api.domain.paciente.Paciente;

public record PacienteDTOList(
        Long id,
        String nombre,
        String email,
        String documento
) {
    public PacienteDTOList(Paciente paciente) {
        this(
                paciente.getId(),
                paciente.getNombre(),
                paciente.getEmail(),
                paciente.getDocumento()
        );
    }
}
