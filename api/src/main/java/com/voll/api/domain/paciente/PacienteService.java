package com.voll.api.domain.paciente;

import com.voll.api.domain.paciente.dto_paciente.PacienteDTO;
import com.voll.api.domain.paciente.dto_paciente.PacienteDTOList;
import com.voll.api.domain.paciente.dto_paciente.PacienteDTOModified;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class PacienteService {

    @Autowired
    private PacienteRepositoy repository;

    public Page<PacienteDTOList> listar(Pageable pageable) {
        return repository.findAllByActivoTrue(pageable).map(PacienteDTOList::new);
    }

    @Transactional
    public Paciente registrar(PacienteData json) {
        return repository.save(new Paciente(json));
    }

    @Transactional
    public PacienteDTO actualizar(PacienteDTOModified datos) {

        Paciente paciente = repository.getReferenceById(datos.id());

        paciente.actualizar(datos);
        return new PacienteDTO(paciente);
    }

    @Transactional
    public void eliminar(Long id) {
        Paciente paciente = repository.getReferenceById(id);
        paciente.eliminar();
    }

    public Paciente buscar(Long id){
        return repository.getReferenceById(id);

    }
}
