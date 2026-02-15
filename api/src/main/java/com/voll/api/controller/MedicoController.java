package com.voll.api.controller;

import com.voll.api.domain.medico.dto_medico.MedicoDTOList;
import com.voll.api.domain.medico.dto_medico.MedicoDTOModified;
import com.voll.api.domain.medico.MedicoData;
import com.voll.api.domain.medico.dto_medico.MedicoDTO;
import com.voll.api.domain.medico.Medico;
import com.voll.api.domain.medico.MedicoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController //Creación del CRUD
@RequestMapping("/medicos") //Este es el path que el controller se va a hacer cargo de mapear
@SecurityRequirement(name = "bearer-key") // Para Header de autorización con SpringDocs
public class MedicoController {

    @Autowired //Inyectamos dependencias de la capa service para aplicar la lógica de negocio
    private MedicoService service;

    //Los @Valid solo van en la capa controller



    /*
        @RequestBody "Toma el JSON que viene en el cuerpo del mensaje y conviértelo automáticamente en un objeto Java de tipo MedicoData"
        @Valid Su función es actuar como un filtro de seguridad antes de que los datos lleguen a tu lógica de negocio.

    *   Al efectuar un nuevo registro en la base de Datos
    *   Retornamos al Usuario un Código HTTP 201 Creaated con un Body
    *   que contiene al registro realizado.
    *
    *   Para devolver un código 201 Created llamamos el metodo save de la capa Service.
    *   Ese método retorna un Objeto Medico, una vez con el objeto almacenado en una variable var.
    *   Creamos la URI, con todos sus componentes que será la dirección o ruta que indicará donde fue creada
    *   la nueva entidad usando la API. Como el ID es variable, usamos el ID del médico que guardamos anteriormente
    * */

    @PostMapping//Como vamos a realizar un registro mapeamos cuando haya un método POST
    public ResponseEntity<MedicoDTO> registrar(@RequestBody @Valid MedicoData json, UriComponentsBuilder uriComponentsBuilder){ //@RequestBody indica que en la request el json es especificamente el body de la Request
        var medico = service.save(json);
        var uri = uriComponentsBuilder
                .path("/medicos/{id}")
                .buildAndExpand(medico.getId())
                .toUri();
        return ResponseEntity.created(uri).body(new MedicoDTO(medico));
    }


    //GET 200 OK
    /*Retorna un 200 OK con un pageable como Body*/
    @GetMapping//Recibe una petición HTTP GET
    public ResponseEntity<Page<MedicoDTOList>> listar(@PageableDefault(size=10, sort={"id"}) Pageable pageable){
        var page = service.listar(pageable);
        return ResponseEntity.ok(page);
    }

    /*PUT 200 OK
    * Modifica un médico en la base de Datos*/
    @PutMapping
    public ResponseEntity<MedicoDTO> modificar(@RequestBody @Valid MedicoDTOModified medico){
        var medicoaux = service.modificar(medico);
        return ResponseEntity.ok(medicoaux);
    }


    //Retorna un 204 No Content
    /*
    * .noContent():
    *
    * Es un método estático que establece
    * automáticamente el código de estado HTTP en 204.
    *
    .build():
    *
    * Es el paso final del patrón Builder.
    * Crea la instancia de ResponseEntity
    * sin incluir nada en el cuerpo de la respuesta.*/
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
         service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    /*GET 200 OK
    * Retorna un Médico en especifico dado su ID*/
    @GetMapping("/{id}")
    public ResponseEntity<MedicoDTO> buscar(@PathVariable Long id){
        Medico medico = service.buscar(id);
        return ResponseEntity.ok(new MedicoDTO(medico));
    }


}
