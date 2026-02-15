package com.voll.api.domain.consulta;

import com.voll.api.domain.medico.Medico;
import com.voll.api.domain.paciente.Paciente;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "consultas")//La tabla en la base de datos se llama "consultas"
@Entity(name = "Consulta")//Cada instancia de una entidad en la tabla "consultas" será un "Consulta"

@Getter //Lombok crea los getter automaticamente
@AllArgsConstructor //Lombok crea un constructor con todos los argumentos y  atributos
@NoArgsConstructor//Lombok crea un constructor vacío
@EqualsAndHashCode(of = "id" )//Dos entidades son iguales si comparten un mismo id
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medico_id")
    private Medico medico;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;

    private LocalDateTime fecha;

    private Boolean activo;

    public Consulta(Medico medico, Paciente paciente, LocalDateTime fecha) {
        this.id = null;
        this.medico = medico;
        this.paciente = paciente;
        this.fecha = fecha;
    }


    public void eliminar() {
        this.activo = false;
    }
}
