package com.voll.api.domain.paciente.dto_paciente;

import com.voll.api.domain.direccion.Direccion;
import com.voll.api.domain.paciente.Paciente;

public record PacienteDTO(
        String nombre,
        String telefono,
        String email,
        String documento,
        Direccion direccion
) {
    public PacienteDTO(Paciente paciente) {
        this(
                paciente.getNombre(),
                paciente.getTelefono(),
                paciente.getEmail(),
                paciente.getDocumento(),
                paciente.getDireccion()
        );
    }
}
