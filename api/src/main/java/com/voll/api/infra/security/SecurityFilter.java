package com.voll.api.infra.security;


import com.voll.api.domain.usuario.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/*
 * Nuestro filtro de seguridad NO ES:
 * - Un Service
 * - Una Configuration
 * - Un Controller
 *
 * Por lo tanto, no utiliza sus respectivas anotaciones.
 *
 * Sin embargo, como Spring necesita gestionarlo dentro de su
 * contexto de beans, se utiliza la anotación genérica @Component.
 *
 * Un @Component indica a Spring que debe crear y administrar
 * una instancia de esta clase dentro de su contenedor.
 */
@Component
public class SecurityFilter extends OncePerRequestFilter {

    //Inyecta las dependencias de Token Service.
    @Autowired
    private TokenService service;

    //Inyecta las dependencias del Repositorio de los usuarios
    @Autowired
    private UsuarioRepository repository;

    /*
     * doFilterInternal es el método encargado de aplicar la lógica
     * del filtro para cada request HTTP.
     *
     * Recibe como parámetros:
     * - HttpServletRequest: la request entrante
     * - HttpServletResponse: la response que será devuelta
     * - FilterChain: la cadena de filtros que deben ejecutarse
     *
     * Si no se invoca filterChain.doFilter(...),
     * la ejecución se detiene en este filtro.
     *
     * 1. FILTRO:
     * Dependiedo si el JWT Token existe o no.
     * Se extraera el subject decodificando el token,
     * se buscara si ese subject está en la base de datos
     * y se llamara a la clase "UsernamePasswordAuthenticationToken"
     * para crear una autenticación, que setteará la autenticación del usuario y la hará valida.
     *
     * De lo contrario se pasará al siguiente filtro y Spring Security lo  manejará.
     */

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var tokenJWT = retrieveToken(request);
        if (tokenJWT != null) {
            var subject = service.getSubject(tokenJWT);
            var user = repository.findByLogin(subject);

            /*
             * Se construye un objeto Authentication utilizando la información del usuario.
             *
             * - No se incluye la contraseña porque el proceso de autenticación
             *   ya fue realizado previamente mediante el JWT.
             * - Se asignan las authorities (roles/permisos) asociadas al usuario,
             *   necesarias para la autorización en capas posteriores.
             */

            var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

            /*
             * Se establece el Authentication en el SecurityContext.
             *
             * A partir de este punto, Spring Security considera al usuario
             * como autenticado para el resto del ciclo de vida de la petición.
             */

            SecurityContextHolder.getContext().setAuthentication(authentication);

            System.out.println(
                    """
                    Paso por el primer filtro de seguridad,
                    se recupero correctamente el Token JWT
                    y se obtuvo la identidad del usuario (Autenticación)!!
                    """
            );
        }
        filterChain.doFilter(request, response);

    }

    /*
     * Este método se encarga de recuperar el JWT desde
     * el header "Authorization" de la request.
     *
     * Elimina el prefijo "Bearer " que es enviado automáticamente
     * por herramientas como Insomnia o clientes HTTP estándar.
     *
     * Si el header no existe, retorna null.
     */

    private String retrieveToken(HttpServletRequest request) {
        var authorizationHeader =  request.getHeader("Authorization");

        if (authorizationHeader != null) {
            return authorizationHeader.replace("Bearer ", "");
        }
        return null;
    }
}
