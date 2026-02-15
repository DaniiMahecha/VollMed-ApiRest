package com.voll.api.domain.consulta.validations;

import com.voll.api.domain.ValidacionException;
import com.voll.api.domain.consulta.ReservaConsultaData;
import org.springframework.stereotype.Component;
import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class ValidadorConsultaConAnticipacion implements ValidadorDeConsultas{

    public void validar(ReservaConsultaData datos){

        var fechaConsulta = datos.fecha();
        var ahora = LocalDateTime.now();

        /* Guarda la diferencia en minutos entre la hora actual y la recibida por el usuario. */
        var diferenciaEnMinutos = Duration.between(ahora, fechaConsulta).toMinutes();

        /* Si la reserva no se hace extricatemnte con 30 minutos de anticipación, no se podrá hacer la reserva. */
        if(diferenciaEnMinutos < 30){
            throw new ValidacionException("Tiene que hacer la reserva con al menos 30 minutos de anticipación");
        }

    }
}
