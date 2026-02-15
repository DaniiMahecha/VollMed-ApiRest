package com.voll.api.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.voll.api.domain.usuario.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;


@Service
public class TokenService {

    /*
     * El secret se obtiene desde application.properties
     * utilizando inyección de valores con @Value.
     */
    @Value("${api.security.token.secret}")
    private String secret;

    /*
    * Este método crea y retorna un token firmado por API Voll.med
    * con vigencia de 2 horas a partir de su creación, para el usuario registrado.
    * Usando HMAC256 como algoritmo de encriptación de la firma.
    * */

    public String generateToken(Usuario usuario) {

        try {
            /*Contraseña especifica y secreta que sirve para Firmar el Token
            * Usando HMAC256 como algoritmo de encriptación*/
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    /*Empresa, Entidad, Compañia que realiza la firma*/
                    .withIssuer("API Voll.med")
                    /*Indica el usuario el cual ha sido registrado*/
                    .withSubject(usuario.getLogin())
                    /*Fecha de expiración*/
                    .withExpiresAt(expireDate())
                    .sign(algorithm);

        } catch (JWTCreationException exception){
            throw new RuntimeException("Error al generar el Token JWT",exception);
        }
    }

    /*
    * Este método permite definir la fecha de expedición del Token 2 horas después de su creación
    * teniedo en cuenta la fecha del dispoitivo local y su zona horaria (Colombia)
    * */

    public Instant expireDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-05:00"));
    }

    /*
     * Este método retorna el Subject decodificado del JWT.
     *
     * Para ello:
     * - Verifica que el token haya sido firmado con el secret correcto
     * - Valida que el issuer coincida con el configurado en la API
     * - Comprueba la validez del token (firma, expiración, integridad)
     *
     * En caso de que alguna de estas validaciones falle,
     * se lanza una excepción en tiempo de ejecución.
     */

    public String getSubject(String tokenJWT) {
        try {
             /* Usando HMAC256 como algoritmo de encriptación*/
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    // specify any specific claim validations
                    .withIssuer("API Voll.med")
                    // reusable verifier instance
                    .build()
                    // Decode the JWT Token
                    .verify(tokenJWT)
                    // Get the subjet of the Token PayLoad
                    .getSubject();
        } catch (JWTVerificationException exception){
            throw new RuntimeException("Error al verificar el Token JWT, Token invalido o expirado");
        }
    }
}
