package com.voll.api.domain.medico;

import com.voll.api.domain.consulta.Consulta;
import com.voll.api.domain.direccion.DireccionData;
import com.voll.api.domain.paciente.Paciente;
import com.voll.api.domain.paciente.PacienteDataTest;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

import static org.assertj.core.api.Assertions.assertThat;


/*
 * Indica que esta clase es un test de la capa de persistencia.
 *
 * @DataJpaTest:
 * - Levanta un contexto de Spring reducido.
 * - Carga únicamente componentes relacionados con JPA
 *   (Entities, Repositories, DataSource).
 * - No inicia el servidor web.
 */
@DataJpaTest

/*
 * Evita que Spring reemplace la base de datos configurada
 * por una base en memoria (H2 u otra).
 *
 * Con esta configuración, el test utilizará la base de datos
 * definida en el perfil activo, manteniendo mayor fidelidad
 * con el entorno real de ejecución.
 */
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)

/*
 * Activa explícitamente el perfil 'test'.
 *
 * Esto le indica a Spring que debe cargar las propiedades
 * definidas en el archivo:
 *
 *   application-test.properties
 *
 * De esta manera, el test se ejecuta en un contexto aislado
 * del entorno de producción, utilizando una configuración
 * específica para pruebas.
 */
@ActiveProfiles("test")
class MedicoRepositoryTest {

    @Autowired
    private MedicoRepository medicoRepository;

    /*
     * EntityManager se utiliza para preparar el estado de la base de datos
     * directamente desde el contexto de persistencia.
     *
     * Nos permite:
     * - Persistir entidades (Médico, Paciente, Consulta)
     * - Controlar exactamente el escenario que queremos probar
     * - Evitar depender de múltiples repositories dentro del test
     *
     * En tests de repositorio, el objetivo es validar la consulta,
     * no la lógica de otros componentes.
     */
    @Autowired
    private EntityManager em;

    @Test
    @DisplayName("""
            Debería devolver null cuando el médico buscado
            existe pero no está disponible en esa fecha.

            Escenario validado:
            - El médico existe.
            - Es de la especialidad solicitada.
            - Tiene una consulta en esa fecha.
            - No hay otro médico disponible.
            """)
    void elegirMedicoAleatorioDisponibleEnLaFechaEscenario1() {

        /*
         * Generamos una fecha dinámica: el próximo lunes a las 10:00.
         *
         * No se utiliza una fecha fija para evitar:
         * - Tests frágiles en el tiempo.
         * - Activar reglas de negocio relacionadas con fechas pasadas.
         */
        var lunesSiguienteALas10 =
                LocalDate.now()
                        .with(TemporalAdjusters.next(DayOfWeek.MONDAY))
                        .atTime(10, 0);

        /*
         * Se persistirse:
         * - Un médico activo de cardiología.
         * - Un paciente.
         * - Una consulta para ese médico exactamente
         *   en lunesSiguienteALas10.
         *
         * De esta forma garantizamos que el médico esté ocupado.
         */

        var medico = registrarMedico("Med1", "med1@gmail.com", "1011111111", Especialidad.CARDIOLOGÍA);
        var paciente = registrarPaciente("Paciente1", "paciente1@gmail.com", "0111111111");
        registrarConsulta(medico, paciente, lunesSiguienteALas10);

        /*
         * Ejecutamos el método bajo prueba.
         *
         * Se busca un médico disponible de cardiología
         * para la fecha indicada.
         */
        var medicoLibre = medicoRepository.elegirMedicoAleatorioDisponibleEnLaFecha(
                        Especialidad.CARDIOLOGÍA,
                        lunesSiguienteALas10
                );

        /*
         * Validamos que el resultado sea null.
         *
         * Esto confirma que:
         * - El médico existe,
         * - Pero no está disponible,
         * - Y el método correctamente representa la ausencia
         *   de disponibilidad devolviendo null.
         */
        assertThat(medicoLibre).isNull();
    }

    @Test
    @DisplayName("""
            Debería devolver un Medico cuando el médico este disponible en esa fecha.
            
            - Retorna un Medico disponible""")
    void elegirMedicoAleatorioDisponibleEnLaFechaEscenario2() {
        var lunesSiguienteALas10 =
                LocalDate.now()
                        .with(TemporalAdjusters.next(DayOfWeek.MONDAY))
                        .atTime(10, 0);

        var medico = registrarMedico("Med1", "med1@gmail.com", "1011111111", Especialidad.CARDIOLOGÍA);


        var medicoLibre = medicoRepository.elegirMedicoAleatorioDisponibleEnLaFecha(
                Especialidad.CARDIOLOGÍA,
                lunesSiguienteALas10
        );

        /*
        Validamos que la consulta traiga a un medico aleatorio disponible.
        En este caso, como el unico médico disponible es "medico".

        retornorá "medico".
        */
        assertThat(medicoLibre).isEqualTo(medico);
    }

    private void registrarConsulta(Medico medico, Paciente paciente, LocalDateTime fecha) {
        em.persist(new Consulta(medico, paciente, fecha));
    }

    private Medico registrarMedico(String nombre, String email, String documento, Especialidad especialidad){
        var medico = new Medico(datosMedico(nombre, email, documento, especialidad));
        em.persist(medico);
        return medico;
    }

    private Paciente registrarPaciente(String nombre, String email, String documento){
        var paciente = new Paciente(datosPaciente(nombre, email, documento));
        em.persist(paciente);
        return paciente;
    }

    private DireccionData datosDireccion(){
        return new DireccionData(
                "calle x",
                "distrito y",
                "ciudad z",
                "Suba",
                "111111",
                "Bogotá",
                "Bogotá"
        );
    }
    private MedicoDataTest datosMedico(String nombre, String email, String documento, Especialidad especialidad){
        return new MedicoDataTest(
                nombre,
                email,
                "3012203162",
                documento,
                especialidad,
                datosDireccion()
        );
    }

    private PacienteDataTest datosPaciente(String nombre, String email, String documento){
        return new  PacienteDataTest(
                nombre,
                email,
                "3012206162",
                documento,
                datosDireccion()
        );
    }


}

