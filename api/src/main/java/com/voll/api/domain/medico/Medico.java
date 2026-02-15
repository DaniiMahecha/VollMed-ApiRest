package com.voll.api.domain.medico;

import com.voll.api.domain.direccion.Direccion;
import com.voll.api.domain.medico.dto_medico.MedicoDTOModified;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Table(name = "medicos")//La tabla en la base de datos se llama "medicos"
@Entity(name = "Medico")//Cada instancia de una entidad en la tabla "medicos" será un "Medico"

@Getter //Lombok crea los getter automaticamente
@AllArgsConstructor //Lombok crea un constructor con todos los argumentos y  atributos
@NoArgsConstructor//Lombok crea un constructor vacío
@EqualsAndHashCode(of = "id" )//Dos entidades son iguales si comparten un mismo id
public class Medico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Boolean activo;
    private String nombre;
    private String telefono;
    private String email;
    private String documento;

    @Enumerated(EnumType.STRING)
    private Especialidad especialidad;

    @Embedded//La relación con la entidad dirección será embedded, quiere decir que la contendrá
    private Direccion direccion;

    public Medico(MedicoData medicoData) {
        this.id = null;
        this.activo = true;
        this.nombre = medicoData.nombre();
        this.telefono = medicoData.telefono();
        this.email = medicoData.email();
        this.documento = medicoData.documento();
        this.especialidad = Especialidad.fromFront(medicoData.especialidad());
        this.direccion = new Direccion(medicoData.direccion());
    }

    public Medico(MedicoDataTest medicoDataTest){
        this.id = null;
        this.activo = true;
        this.nombre = medicoDataTest.nombre();
        this.telefono = medicoDataTest.telefono();
        this.email = medicoDataTest.email();
        this.documento = medicoDataTest.documento();
        this.especialidad = medicoDataTest.especialidad();
        this.direccion = new Direccion(medicoDataTest.direccion());
    }

    public void actualizarMedico(MedicoDTOModified datos){
        if (datos == null) {
            return;
        }
        if (datos.nombre() != null){
            this.nombre = datos.nombre();
        }
        if (datos.telefono() != null){
            this.telefono = datos.telefono();
        }
        if (datos.direccion() != null){
            this.direccion.actualizarDirección(datos.direccion());
        }
    }

    public void eliminar() {
        this.activo = false;
    }
}
