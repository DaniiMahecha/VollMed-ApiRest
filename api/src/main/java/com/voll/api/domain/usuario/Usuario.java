package com.voll.api.domain.usuario;

import com.voll.api.domain.usuario.dto_usuario.UsuarioDTO;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Table(name = "usuarios")//La tabla en la base de datos se llama "usuarios"
@Entity(name = "Usuario")//Cada instancia de una entidad en la tabla "usuarios" será un "Usuario"
@Getter //Lombok crea los getter automaticamente
@AllArgsConstructor //Lombok crea un constructor con todos los argumentos y  atributos
@NoArgsConstructor//Lombok crea un constructor vacío
@EqualsAndHashCode(of = "id" )//Dos entidades son iguales si comparten un mismo id

/*
* La interaz UserDetails Provides core user information.
* They simply store user information which is later encapsulated into Authentication objects.
* This allows non-security related user information (such as email addresses, telephone numbers etc) to be stored in a convenient location.
* */
public class Usuario implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String login;
    private String password;

    public Usuario(UsuarioDTO usuarioDTO) {
        this.id = null;
        this.login = usuarioDTO.login();
        this.password = usuarioDTO.password();


    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public String getPassword() {
        return password;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
