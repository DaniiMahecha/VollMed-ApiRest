package com.voll.api.domain.consulta.dto_consulta;

import com.voll.api.domain.consulta.Consulta;

import java.time.LocalDateTime;

public record ConsultaDTO(Long id,
                          Long idMedico,
                          Long idPaciente,
                          LocalDateTime fecha) {

    public ConsultaDTO(Consulta consulta) {
        this(
                consulta.getId(),
                consulta.getMedico().getId(),
                consulta.getPaciente().getId(),
                consulta.getFecha()
        );
    }
}
