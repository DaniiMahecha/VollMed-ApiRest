package com.voll.api.domain.consulta.validations;

import com.voll.api.domain.ValidacionException;
import com.voll.api.domain.consulta.ConsultaRepository;
import com.voll.api.domain.consulta.ReservaConsultaData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidadorPacienteSinOtraConsultaEnElMismoDia implements ValidadorDeConsultas{

    @Autowired
    private ConsultaRepository repository;

    public void validar(ReservaConsultaData datos){

        /*
        Determino un rango con estas dos variables.
        El punto de partida es 'primerHorario' y el punto final es 'ultimoHorario'
        'primerHorario'<---------------------------------------------------->'ultimoHorario'

        Des esta manera podemos verificar si un usuario tiene más de una consulta en el rango
        comprendido entre 'primerHorario' y 'ultimoHorario'
        */
        var primerHorario = datos.fecha().withHour(7);
        var ultimoHorario = datos.fecha().withHour(18);

        /*
        Se utiliza una derived query con `existsBy` para verificar la existencia
        de al menos una consulta del paciente, sin necesidad de cargar
        la entidad completa desde la base de datos.

        El uso de `FechaBetween` permite acotar la búsqueda a un rango temporal
        específico (inicio y fin del día), expresando de forma clara
        la regla de negocio: un paciente no puede tener más de una consulta
        en el mismo día.
        */
        var pacienteTieneOtraConsultaEnElDia = repository.existsByPacienteIdAndFechaBetween(
                datos.idPaciente(),
                primerHorario,
                ultimoHorario);

        /*
        Si el Paciente ya tiene una consulta agendada lanzaremos un Exception , expresando
        de forma clara la regla de negocio:

        un paciente no puede tener más de una consulta en el mismo día.
        */
        if(pacienteTieneOtraConsultaEnElDia){
            throw new ValidacionException("El paciente ya tiene una consulta agendada para este día");
        }
    }
}
