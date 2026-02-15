package com.voll.api.domain.medico.dto_medico;

import com.voll.api.domain.medico.Especialidad;
import com.voll.api.domain.medico.Medico;

public record MedicoDTOList(
        Long id,
        String nombre,
        String email,
        String documento,
        Especialidad especialidad
) {

    public MedicoDTOList(Medico medico) {
        this(
                medico.getId(),
                medico.getNombre(),
                medico.getEmail(),
                medico.getDocumento(),
                medico.getEspecialidad()
        );
    }
}
