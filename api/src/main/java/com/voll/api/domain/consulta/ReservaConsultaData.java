package com.voll.api.domain.consulta;


import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

/*
* Este record representa los datos del Body de la request para programar una consulta.
* La notación @Future indica que la fecha tiene que ser no anterior a la fecha de la maquina.
*/

public record ReservaConsultaData(
        Long idMedico,
        @NotNull
        Long idPaciente,
        @NotNull
        @Future
        LocalDateTime fecha,
        String especialidad
) {}
