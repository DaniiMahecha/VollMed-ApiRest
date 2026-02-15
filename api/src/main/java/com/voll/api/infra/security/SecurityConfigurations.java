package com.voll.api.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration // Indica que esta clase contiene configuraciones de Spring
@EnableWebSecurity // Habilita y permite personalizar la configuración de Spring Security
public class SecurityConfigurations {

    @Autowired // Inyección de datos llamando a SecurityFilter
    private SecurityFilter securityFilter;
    /*
     * Este método deshabilita el sistema de protección contra ataques CSRF,
     * ya que nuestra API es Stateless, es decir, no utiliza cookies
     * ni sesiones HTTP para el proceso de autenticación.
     *
     * Al trabajar con JWT, el token se envía explícitamente en cada request,
     * por lo que el mecanismo de CSRF deja de aplicar.
     *
     * Además:
     * - Se configura la aplicación como Stateless.
     * - Se permite el acceso público a la ruta "/login" mediante POST.
     * - El resto de las rutas solo pueden ser accedidas por usuarios autenticados.
     */

    @Bean // Expone este método como un Bean gestionado por Spring
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(csrf -> csrf.disable() )

                // Cambio de SessionCreatioPolicy a STATELESS, es decir, no aalmacena datos del usurio en la BD
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Se especifican las reglas de autorización
                .authorizeHttpRequests(autherizationRequest -> {
                    autherizationRequest.requestMatchers(HttpMethod.POST, "/login").permitAll()
                    //SpringDocs
                    .requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll()
                    .anyRequest().authenticated();
                })

                /*
                 * Se registra el filtro de seguridad personalizado dentro de la cadena de filtros de Spring Security.
                 *
                 * Este filtro se ejecutará ANTES de UsernamePasswordAuthenticationFilter, ya que:
                 * - Implementa la lógica de autenticación basada en JWT.
                 * - Necesita establecer el Authentication en el SecurityContext
                 *   antes de que Spring procese cualquier mecanismo estándar de autenticación.
                 */

                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /*
    * Este método expone a Spring Boot la manera de crear un AuthenticationManager
    */

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /*
    * Disponibilizamos un método que permite usar BCRYPT como un password encoder
    */

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
