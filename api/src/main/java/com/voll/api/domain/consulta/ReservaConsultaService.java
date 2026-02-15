package com.voll.api.domain.consulta;

import com.voll.api.domain.ValidacionException;
import com.voll.api.domain.consulta.dto_consulta.ConsultaDTO;
import com.voll.api.domain.consulta.validations.ValidadorDeConsultas;
import com.voll.api.domain.medico.Especialidad;
import com.voll.api.domain.medico.Medico;
import com.voll.api.domain.medico.MedicoRepository;
import com.voll.api.domain.paciente.PacienteRepositoy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReservaConsultaService {

    @Autowired
    private ConsultaRepository consultaRepository;
    @Autowired
    private MedicoRepository medicoRepository;
    @Autowired
    private PacienteRepositoy pacienteRepositoy;
    @Autowired
    private List<ValidadorDeConsultas> validadores;

    @Transactional
    public ConsultaDTO reservar(ReservaConsultaData datos){
        //---------------------------------VALIDACIONES----------------------------------
        if(!pacienteRepositoy.existsById(datos.idPaciente())){
            throw new ValidacionException("No existe un paciente con el id informado");
        }

        if(datos.idMedico() != null && !medicoRepository.existsById(datos.idMedico())){
            throw new ValidacionException("No existe un médico con el id informado");
        }

        validadores.forEach(validador -> validador.validar(datos));
        //---------------------------------------------------------------------------------
        var medico = elegirMedico(datos);

        /* Si no se envió el id del médico y no hay más médicos disponibles, lanza la excepción */
        if(datos.idMedico() == null){
            throw new ValidacionException("No existe un médico disponible en ese horario");
        }

        var paciente = pacienteRepositoy.findById(datos.idPaciente()).get();


        Consulta consulta = new Consulta(null, medico, paciente, datos.fecha(), true);

        consultaRepository.save(consulta);
        return new ConsultaDTO(consulta);
    }


    /*
    * Este método se encarga de retorar un médico aleatorio en caso de que no se especifique uno.
    * El médico estará disponible para la fecha seleccionada.
    *
    * Si no se especificá el médico, se deberá especificar la especialidad a la que quiere agendar la consulta
    */
    private Medico elegirMedico(ReservaConsultaData datos) {
        if(datos.idMedico() != null){
            return medicoRepository.getReferenceById(datos.idMedico());
        }

        if(datos.especialidad() == null){
            throw new ValidacionException("Es necesario escoger una especialidad cuando no se elige un médico");
        }

        /*
        * Este return devulve a un médico aleatorio que esta disponible para la fecha indicada
        * especificando la especialidad y la fecha de la consulta.
        */
        return medicoRepository.elegirMedicoAleatorioDisponibleEnLaFecha(Especialidad.fromFront((datos.especialidad())), datos.fecha());
    }

    @Transactional
    public void eliminarConsulta(Long idConsulta){
        Consulta consulta = consultaRepository.getReferenceById(idConsulta);
        consulta.eliminar();
    }
}
