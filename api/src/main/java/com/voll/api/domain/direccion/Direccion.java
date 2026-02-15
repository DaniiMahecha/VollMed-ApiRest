package com.voll.api.domain.direccion;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter //Lombok crea los getter automaticamente
@AllArgsConstructor //Lombok crea un constructor con todos los argumentos y  atributos
@NoArgsConstructor//Lombok crea un constructor vacío
@Embeddable//Sera embeddable por la entidad Medico
public class Direccion {
    private String calle;
    private String numero;
    private String complemento;
    private String barrio;

    @Column(name = "codigo_postal", nullable = false) //En las bases de datos el espacio entre palabras es con _, y no es nullable por logica de negocio
                                                      //Cuando usas snake_case en SQL y camelCase en Java, SIEMPRE usa @Column(name = "...").
    private String codigoPostal;
    private String ciudad;

    private String estado;

    public Direccion(DireccionData direccion) {
        this.calle = direccion.calle();
        this.numero = direccion.numero();
        this.complemento = direccion.complemento();
        this.barrio = direccion.barrio();
        this.codigoPostal = direccion.codigoPostal();
        this.ciudad = direccion.ciudad();
        this.estado = direccion.estado();
    }

    public void actualizarDirección(DireccionData datos) {
        if (datos == null) {
            return;
        }
        if (datos.calle() != null) {
            this.calle = datos.calle();
        }
        if (datos.numero() != null) {
            this.numero = datos.numero();
        }
        if (datos.complemento() != null) {
            this.complemento = datos.complemento();
        }
        if (datos.barrio() != null) {
            this.barrio = datos.barrio();
        }
        if (datos.codigoPostal() != null) {
            this.codigoPostal = datos.codigoPostal();
        }
        if (datos.ciudad() != null) {
            this.ciudad = datos.ciudad();
        }
        if (datos.estado() != null) {
            this.estado = datos.estado();
        }
    }
}
