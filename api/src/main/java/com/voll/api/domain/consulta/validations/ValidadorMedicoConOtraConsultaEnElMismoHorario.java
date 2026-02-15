package com.voll.api.domain.consulta.validations;

import com.voll.api.domain.ValidacionException;
import com.voll.api.domain.consulta.ConsultaRepository;
import com.voll.api.domain.consulta.ReservaConsultaData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidadorMedicoConOtraConsultaEnElMismoHorario implements ValidadorDeConsultas{

    @Autowired
    private ConsultaRepository repository;

    public void validar(ReservaConsultaData datos){

        /*
         * Esta variable almacena el resultado de una derived query basada en `existsBy`,
         * la cual verifica si el médico indicado ya tiene una consulta agendada
         * en la fecha y hora especificadas.
         *
         * El objetivo de esta validación es garantizar la regla de negocio de
         * no permitir que un médico tenga más de una consulta en el mismo
         * horario.
         *
         * - Si el médico no tiene una consulta en ese horario, el flujo continúa
         *   y se puede agendar la nueva consulta.
         * - Si ya existe una consulta, se lanzará una excepción.
         */
        var medicoTieneOtraConsultaEnElMismoHorario = repository.existsByMedicoIdAndFecha(
                datos.idMedico(),
                datos.fecha()
        );

        /*
         * Se lanza una excepción cuando la validación detecta que el médico
         * ya tiene una consulta asignada en la misma fecha y hora.
         * Esto evita conflictos de agenda y mantiene la consistencia del negocio.
         */
        if (medicoTieneOtraConsultaEnElMismoHorario){
            throw new ValidacionException("Médico ya tiene otra consulta en esa misma fecha y hora");
        }

    }
}
