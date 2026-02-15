package com.voll.api.domain.consulta.validations;

import com.voll.api.domain.ValidacionException;
import com.voll.api.domain.consulta.ReservaConsultaData;
import com.voll.api.domain.medico.MedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidadorMedicoActivo implements ValidadorDeConsultas{

    @Autowired
    private MedicoRepository repository;

    public void validar(ReservaConsultaData datos){

        /*
        Si el ID del médico es null, entonces se seleccionará aleatoriamente un médico.
        hacemos return para que se encargue la validación del médico aleatorio.
        */
        if(datos.idMedico() == null){
            return;
        }

        /*
        Guarda un valor booleano.
        Se encarga de preguntar en el repository si el médico se encuentra activo
        - True : Si lo está
        - False : No lo está
        */
        var medicoEstaActivo = repository.findActivoById(datos.idMedico());
        if (!medicoEstaActivo){
            throw new ValidacionException("No se puede realizar una consulta con un médico que NO se encuentre ACTIVO");
        }
    }
}
