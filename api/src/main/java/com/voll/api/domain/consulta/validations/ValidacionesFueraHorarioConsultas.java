package com.voll.api.domain.consulta.validations;

import com.voll.api.domain.ValidacionException;
import com.voll.api.domain.consulta.ReservaConsultaData;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;

@Component
public class ValidacionesFueraHorarioConsultas implements ValidadorDeConsultas{

    public void validar(ReservaConsultaData datos){

        /* Guarda la fecha de la consulta entregada por el Front-end */
        var fechaConsulta = datos.fecha();

        /*
        Guarda el valor booleano.
        True si es dia indicado es Domingo, False sí no lo es
        */
        var domingo = fechaConsulta.getDayOfWeek().equals(DayOfWeek.SUNDAY);

        /*
        Guarda el valor booleano.
        True si la hora indicáda por el usuario es anterior a la hora de apertura
        False sí no es así
        */
        var horarioDeAntesDeApertura = fechaConsulta.getHour() < 7;

        /*
        Guarda el valor booleano.
        True si la hora indicáda por el usuario es posterior o igual a la hora de cierre
        False sí no es así.

        Aunque la hora de cierre sea a las 19, las consultas durán una hora
        por lo mismo por máximo un usuario puede reservar a las 18.

        Para que de esta manera se respete el horario de cierre
        */
        var horaioDespuesDeCierre = fechaConsulta.getHour() > 18;


        /*
        Si la hora o fecha seleccionada por el usuario:
        - Es Domingo
        - Es antes de la hora de apertura
        - Es posterior o igual a la hora de cierre
        Lanzaremos una Exception propia, indicando que son horarios fuera de atención.
        */
        if(domingo || horarioDeAntesDeApertura || horaioDespuesDeCierre){
            throw new ValidacionException("Horario seleccionado fuera de atención");
        }
    }
}
