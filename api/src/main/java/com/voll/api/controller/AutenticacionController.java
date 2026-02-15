package com.voll.api.controller;

import com.voll.api.domain.usuario.Usuario;
import com.voll.api.domain.usuario.UsuarioRepository;
import com.voll.api.domain.usuario.dto_usuario.UsuarioDTO;
import com.voll.api.infra.security.TokenJWTDTO;
import com.voll.api.infra.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AutenticacionController {

    @Autowired
    private TokenService service;
    @Autowired
    private AuthenticationManager manager;


    /*
    * Este método recibe un verbo POST HTTP
    * para el registro de un usuario
    *
    * Interpreta el body del método POST y usa @Valid para validar sus datos
    * los datos del usuario se representan en un DTO
    *
    * Inyectamos AutehnticationManager y usamos .authenticate, un método que entiende
    * las credenciales del usuario de manera especial, para validar y permitir o no el acceso al usario
    *
    * El token es intanciado con los datos del usuario
    * */

    @PostMapping
    public ResponseEntity<?> autenticar(@RequestBody @Valid UsuarioDTO usuario) throws Exception{
        UsernamePasswordAuthenticationToken tokenAuthentication = new UsernamePasswordAuthenticationToken(usuario.login(), usuario.password());
        /*
        El método `authenticate` puede producir tres resultados posibles:
        - Éxito:
          Las credenciales son correctas y se devuelve un objeto `Authentication` completamente construido, incluyendo los roles o permisos del usuario.

        - Error:
          Si la contraseña es incorrecta, se lanza una excepción `BadCredentialsException`.

        - Indefinido:
          Si el manager no puede manejar ese tipo de autenticación, puede devolver `null` (caso menos común).
        * */

        Authentication authentication = manager.authenticate(tokenAuthentication);

        /*Autentication devuelve un Objeto que deberá ser Casteado indicando que será un Usuario
          el usuario lo usara el service para generar el Token (String)*/
        String tokenJWT = service.generateToken((Usuario) authentication.getPrincipal());

        /*Siguiendo los estandares la información será enviado como un DTO*/
        return  ResponseEntity.ok(new TokenJWTDTO(tokenJWT));
    }
}
