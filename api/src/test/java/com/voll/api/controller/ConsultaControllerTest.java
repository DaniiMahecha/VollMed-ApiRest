package com.voll.api.controller;

import com.voll.api.domain.consulta.ReservaConsultaData;
import com.voll.api.domain.consulta.ReservaConsultaService;
import com.voll.api.domain.consulta.dto_consulta.ConsultaDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest // Notación que levanta todo el contexto de Spring para poder hacer Test
                // en los controllers.
                // A diferencia de DataJPATest, que solo levanta el contexto de componentes
                // relacionados a JPA.

@AutoConfigureMockMvc // Le indica a Spring que va usar MockMvc
@AutoConfigureJsonTesters // Le indica a Spring que va usar JsonTesters
class ConsultaControllerTest {
    /*
    MockMvc parecido a EntityManager, nos permite hacer uso de ConsulController
    directamente haciendo los test de forma aislada.
    */
    @Autowired
    private MockMvc mvc;

/*
JacksonTester sirve para crear JSON a traves de clases que tenemos en nuestro proyecto
*/

    /*
    Tipo de dato que recibimos para crear la consulta, input
    */
    @Autowired
    private JacksonTester<ReservaConsultaData> reservaConsultaDataJson;

    /*
    Tipo de dato que devolvemos después de que se creó la consulta, output
    */
    @Autowired
    private JacksonTester<ConsultaDTO> consultaDTOJson;

    /*
    ¿Qué hace realmente @MockBean?
    - Reemplaza el bean real dentro del ApplicationContext.
    - Inyecta un mock de Mockito en su lugar.
    - Permite controlar su comportamiento dentro del test.

    Esto implica que:
    - El Controller sigue funcionando normalmente.
    - Pero el Service real NO se ejecuta.
    - No se evalúan reglas de negocio.
    - No se accede a base de datos.
    - Este test queda aislado en la capa web.

    No estamos probando negocio.
    Estamos probando comportamiento HTTP.
    */
    @MockBean
    private ReservaConsultaService reservaConsultaService;

    @Test
    @DisplayName("Debería devolver 400 cuando la request no tenga datos")
    @WithMockUser // Le comunicámos al Test que no nos interesa logearnos para los test de las request
    void reservar_escenatio1() throws Exception {
        //perform nos permite usar los verbos HTTP para testear el controller
        //MockMvcRequestBuilder
        var response = mvc.perform(post("/consulta"))
                .andReturn().getResponse();

        // Con assertThat, queremos validar que el status de 'response', seá una BAD_REQUES 400
        // Debido a que no estamos enviando un body.
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST. value());
    }

    @Test
    @DisplayName("Debería devolver 200 cuando la request reciba un json valido")
    @WithMockUser
    void reservar_escenatio2() throws Exception {

        var fecha = LocalDateTime.now().plusHours(1); //Fecha actual más una hora
        var consultaDTO = new ConsultaDTO(null, 2L, 5L, fecha);

        /*Mockito - Gracias a que mockeamos el Servie que se encarga de crear una consulta, es decir, no se tiene en cuenta la lógica de negocio.
        * No se está creando un JSON response. Por eso implementaremos este código que */
        when(reservaConsultaService.reservar(any/*Reserva recibe un DTO que representa el body de la request,
                                                pero como no nos interesa implementar la lógica completa,
                                                debido a que lo mockeamos, entonces colocamos any(), que indica que
                                                'algo' pasa como parámetro */())).thenReturn(consultaDTO);


       /*
       Esta variable representa la request de una consulta
       */
        var request = mvc.perform(post("/consulta")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reservaConsultaDataJson.write(new ReservaConsultaData(
                                2L,
                                5L,
                                fecha,
                                "dermatología"


                        )).getJson() // Devuelve un JSON
                        /*Escribe el JSON recibido para registrar la consulta, input*/
                        )) // Indicamos que el contenido del Body es un JSON
                .andReturn().getResponse();

        /*
         * Serializamos el DTO esperado a JSON.
         *
         * Esto nos permite construir explícitamente el contenido que
         * el controller debería devolver en el cuerpo de la respuesta.
         *
         * De esta forma validamos no solo el status HTTP,
         * sino también la correcta serialización del objeto.
         */
        var jsonResponse = consultaDTOJson
                .write(consultaDTO)
                .getJson();

        /*
         * Verificamos que el endpoint responda con 200 OK.
         *
         * Esto confirma que la operación fue exitosa
         * desde el punto de vista HTTP.
         */
        assertThat(request.getStatus())
                .isEqualTo(HttpStatus.OK.value());

        /*
         * Verificamos que el contenido del response coincida exactamente
         * con el JSON esperado.
         *
         * Aquí estamos validando:
         * - La estructura del payload
         * - La serialización correcta
         * - Que el controller devuelve el DTO adecuado
         */
        assertThat(request.getContentAsString())
                .isEqualTo(jsonResponse);
    }
    @Test
    void cancelar() {
    }
}