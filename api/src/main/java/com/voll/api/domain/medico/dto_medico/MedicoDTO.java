package com.voll.api.domain.medico.dto_medico;

import com.voll.api.domain.direccion.Direccion;
import com.voll.api.domain.medico.Especialidad;
import com.voll.api.domain.medico.Medico;

public record MedicoDTO(
        Long id,
        String nombre,
        String email,
        String documento,
        String telefono,
        Especialidad especialidad,
        Direccion direccion
) {
    public MedicoDTO(Medico medico){
        this(
                medico.getId(),
                medico.getNombre(),
                medico.getEmail(),
                medico.getDocumento(),
                medico.getTelefono(),
                medico.getEspecialidad(),
                medico.getDireccion()
        );
    }
}
