package com.voll.api.domain.medico;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

/*<Entidad que guardaremos, Tipo del ID*/
public interface MedicoRepository extends JpaRepository<Medico, Long>  {
    Page<Medico> findAllByActivoTrue(Pageable pageable);

    /*
     * Esta anotación @Query define una consulta JPQL personalizada.
     * Se utiliza cuando la lógica de selección no puede expresarse
     * fácilmente con los métodos derivados de Spring Data JPA.
     */
    @Query("""
    /*
     * Seleccionamos la entidad Medico (m) desde la tabla correspondiente.
     * JPQL trabaja con entidades y sus atributos, no con tablas SQL directamente.
     */
    SELECT m FROM Medico m 
    WHERE
        /*
         * Filtramos solo médicos activos.
         * Se asume que 'activo = 1' representa un médico habilitado.
         */
        m.activo = true
        AND
        /*
         * Filtramos por la especialidad recibida como parámetro.
         * ':especialidad' se enlaza con el argumento del método.
         */
        m.especialidad = :especialidad
        AND
        /*
         * Excluimos médicos que ya tengan una consulta asignada
         * en la fecha indicada.
         */
        m.id NOT IN (
            /*
             * Subconsulta que obtiene los IDs de los médicos
             * que ya tienen una consulta en la fecha dada.
             */
            SELECT c.medico.id FROM Consulta c
            WHERE
                /*
                 * Se comparan las fechas de la consulta con la fecha solicitada.
                 * Si un médico aparece aquí, no estará disponible.
                 */
                c.fecha = :fecha
        )
    /*
     * Ordenamos el resultado de forma aleatoria.
     * IMPORTANTE: 'rand()' puede tener impacto en rendimiento
     * si el volumen de datos es grande.
     */
    ORDER BY rand()
    /*
     * Limitamos el resultado a un solo médico.
     * La idea es obtener un único médico disponible al azar.
     */
    LIMIT 1
    """)
    /*
     * Método del repositorio que devuelve un Medico.
     *
     * - especialidad: especialidad requerida para la consulta.
     * - fecha: fecha futura en la que se necesita disponibilidad.
     * - NULL : No encuentra resultado esperado
     */
    Medico elegirMedicoAleatorioDisponibleEnLaFecha(Especialidad especialidad, LocalDateTime fecha);

    /*
     * Consulta JPQL cuyo objetivo es obtener únicamente
     * el estado "activo" de un médico específico.
     *
     * IMPORTANTE:
     * - No se devuelve la entidad Medico completa
     * - Solo se retorna el valor del atributo m.activo
     *
     * Esto es una optimización intencional cuando
     * solo se necesita un dato puntual y no todo el objeto.
     */
    @Query("""
    /*
     * Seleccionamos exclusivamente el campo 'activo'
     * de la entidad Medico.
     *
     * En JPQL se pueden proyectar atributos específicos,
     * no es obligatorio devolver la entidad completa.
     */
    SELECT m.activo
    FROM Medico m
    WHERE
        /*
         * Filtramos por el identificador del médico.
         * ':idMedico' se enlaza con el parámetro del método.
         */
        m.id = :idMedico
    """)

    boolean findActivoById(Long idMedico);
}
