package com.voll.api.controller;

import com.voll.api.domain.consulta.ReservaConsultaService;
import com.voll.api.domain.consulta.dto_consulta.ConsultaDTO;
import com.voll.api.domain.consulta.ReservaConsultaData;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/consulta")
@SecurityRequirement(name = "bearer-key") // Para Header de autorización con SpringDocs
public class ConsultaController {

    @Autowired
    private ReservaConsultaService service;
    /*
    * Este método recibe una request POST
    */

    @PostMapping
    public ResponseEntity<?> reservar(@RequestBody @Valid ReservaConsultaData datos){
        System.out.println(datos);
        var detalleConsulta = service.reservar(datos);
        return ResponseEntity.ok(detalleConsulta);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelar(@PathVariable Long id){
        System.out.println(id);
        service.eliminarConsulta(id);
        return ResponseEntity.noContent().build();
    }
}
