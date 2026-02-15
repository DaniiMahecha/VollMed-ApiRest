package com.voll.api.domain.medico;


import com.voll.api.domain.direccion.DireccionData;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record MedicoDataTest(
        @NotBlank
        String nombre,

        @NotBlank
        @Pattern(regexp = "\\d{10}")
        String telefono,

        @NotBlank @Email
        String email,
        //@Email indica que debe conservar las propiedades de una email

        @NotBlank
        @Pattern(regexp = "\\d{8,10}")
        String documento,
        //El documento tiene un patrón y debe contener si o si 10 caracteres

        @NotBlank
        Especialidad especialidad,

        @NotNull @Valid
        DireccionData direccion
        //Ahora Validaremos los campos de DireccionData
){


}
