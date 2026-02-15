package com.voll.api.domain.paciente;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PacienteRepositoy extends JpaRepository<Paciente, Long> {
    Page<Paciente> findAllByActivoTrue(Pageable pageable);

    @Query("""
    /*
     * Seleccionamos exclusivamente el campo 'activo'
     * de la entidad Paciente.
     *
     * En JPQL se pueden proyectar atributos específicos,
     * no es obligatorio devolver la entidad completa.
     */
    SELECT p.activo
    FROM Paciente p
    WHERE
        /*
         * Filtramos por el identificador del médico.
         * ':idMedico' se enlaza con el parámetro del método.
         */
        p.id = :idPaciente
    """)
    boolean findActivoById(Long idPaciente);
}
