package com.voll.api.domain.consulta.validations;

import com.voll.api.domain.ValidacionException;
import com.voll.api.domain.consulta.ReservaConsultaData;
import com.voll.api.domain.paciente.PacienteRepositoy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidadorPacienteActivo implements ValidadorDeConsultas{

    /* Inyección de datos del PacienteRepository*/
    @Autowired
    PacienteRepositoy repositoy;

    public void validar(ReservaConsultaData datos){

        /*
        Guarda un valor booleano.
        Se encarga de preguntar en el repository si el paciente se encuentra activo
        - True : Si lo está
        - False : No lo está
        */
        var pacienteEstaActivo = repositoy.findActivoById(datos.idPaciente());
        if(!pacienteEstaActivo){
            throw new ValidacionException("No se puede realizar una consulta con un paciente que NO se encuentre ACTIVO");
        }
    }
}
