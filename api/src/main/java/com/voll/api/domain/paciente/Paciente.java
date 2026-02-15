package com.voll.api.domain.paciente;

import com.voll.api.domain.direccion.Direccion;
import com.voll.api.domain.paciente.dto_paciente.PacienteDTOModified;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "pacientes")
@Entity(name = "Paciente")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Paciente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean activo;
    private String nombre;
    private String email;
    private String telefono;

    @Column(name = "documento_identidad", nullable = false)
    private String documento;

    @Embedded
    private Direccion direccion;

    public Paciente(PacienteData pacienteData) {
        this.id = null;
        this.activo = true;
        this.nombre = pacienteData.nombre();
        this.email = pacienteData.email();
        this.telefono = pacienteData.telefono();
        this.documento = pacienteData.documento();
        this.direccion = new Direccion(pacienteData.direccion());
    }

    public Paciente(PacienteDataTest pacienteDataTest) {
        this.id = null;
        this.activo = true;
        this.nombre = pacienteDataTest.nombre();
        this.email = pacienteDataTest.email();
        this.telefono = pacienteDataTest.telefono();
        this.documento = pacienteDataTest.documento();
        this.direccion = new Direccion(pacienteDataTest.direccion());
    }

    public void actualizar(PacienteDTOModified datos) {
        if (datos == null) {
            return;
        }
        if (datos.nombre() != null) {
            this.nombre = datos.nombre();
        }
        if (datos.telefono() != null) {
            this.telefono = datos.telefono();
        }
        if (datos.direccion() != null) {
            this.direccion.actualizarDirección(datos.direccion());
        }
    }

    public void eliminar() {
        this.activo = false;
    }
}
