package com.voll.api.domain.medico;

import com.voll.api.domain.medico.dto_medico.MedicoDTO;
import com.voll.api.domain.medico.dto_medico.MedicoDTOList;
import com.voll.api.domain.medico.dto_medico.MedicoDTOModified;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
;


@Service
public class MedicoService {
    @Autowired
    private MedicoRepository repository;

    //Los @Transactional solo van en la capa Service


    //POST
    @Transactional //El de Spring Boot
    //Se usa siempre que se modifiquen datos en la BD
    //Cada vez que realicemos este tipo de operaciones toca anotar con @Transactional
    //Regla básica: toda operación de escritura en base de datos debe ser transaccional.
    //Guardar en la BD.
    //Retornamos La entidad JPA para luego tratarla en el Controller con ResponseEntity
    public Medico save(MedicoData json) {
        return repository.save(new Medico(json));
    }

    //GET
    //Devolver un página con 10 MédicoDTOList como máximo 200 OK con Body
    public Page<MedicoDTOList> listar(Pageable pageable) {
        return repository.findAllByActivoTrue(pageable).map(MedicoDTOList::new);
    }

    //PUT | PATCH
    @Transactional
    //Modifica solo el nombre o telefono o dirección del medico seleccionado por su Id, devuelve un 200 OK con el médico actualizado
    public MedicoDTO modificar(MedicoDTOModified datos) {

        Medico medico = repository.getReferenceById(datos.id());

        medico.actualizarMedico(datos);
        return new MedicoDTO(medico);
    }

    //DELETE
    @Transactional
    public void eliminar(Long id) {
        Medico medico = repository.getReferenceById(id);
        medico.eliminar();

    }

    public Medico buscar(Long id) {
        return repository.getReferenceById(id);
    }
}
