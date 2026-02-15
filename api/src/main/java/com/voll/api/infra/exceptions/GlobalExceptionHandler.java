package com.voll.api.infra.exceptions;


import com.voll.api.domain.ValidacionException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice // Notación que indica que se trata de un controlador de errores
public class GlobalExceptionHandler {

    /**
     * Retorna un Código 404 NOT FOUND en caso de que getReferenceById no encuentre una entidad por si ID y su Proxy no carge
     */
    @ExceptionHandler(EntityNotFoundException.class) //Capturamos las excepciones de la siguiente clase
     public ResponseEntity<Void> Manejo404(){
         return  ResponseEntity.notFound().build();
     }

    // Maneja errores de validación y retorna un 400 Bad Request
    /*
     * @ExceptionHandler:
     *
     * Indica que este método se ejecutará automáticamente
     * cuando ocurra una excepción de tipo
     * MethodArgumentNotValidException.
     *
     * MethodArgumentNotValidException:
     *
     * Se lanza cuando una request no cumple con las
     * validaciones definidas con @Valid.
     */

     @ExceptionHandler(MethodArgumentNotValidException.class)
     public ResponseEntity<List<badValidationDTO>> Manejo400(MethodArgumentNotValidException ex){
         // Obtiene la lista de errores de validación por campo
         /*
          * getFieldErrors():
          *
          * Retorna una lista de FieldError, donde cada elemento
          * representa un campo inválido en la request.
          */
        var errores = ex.getFieldErrors();
         // Retorna un 400 Bad Request con errores controlados
         /*
          * badRequest():
          *
          * Establece el código de estado HTTP en 400.
          *
          * body():
          *
          * Define el cuerpo de la respuesta.
          *
          * stream() + map():
          *
          * Convierte cada FieldError en un DTO,
          * exponiendo solo la información necesaria.
          */
        return  ResponseEntity.badRequest().body(errores.stream().map(badValidationDTO::new).toList());
     }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity gestionarError400(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity gestionarErrorBadCredentials() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity gestionarErrorAuthentication() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Falla en la autenticación");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity gestionarErrorAccesoDenegado() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso denegado");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity gestionarError500(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " +ex.getLocalizedMessage());
    }

    @ExceptionHandler(ValidacionException.class)
    public ResponseEntity gestionarErrorValidacion(ValidacionException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }



     public record badValidationDTO(String field, String message){
        public badValidationDTO(FieldError fieldError){
            this(
                    fieldError.getField(),
                    fieldError.getDefaultMessage()
            );

        }
     }
}
