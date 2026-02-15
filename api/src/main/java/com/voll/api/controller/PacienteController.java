package com.voll.api.controller;

import com.voll.api.domain.paciente.dto_paciente.PacienteDTO;
import com.voll.api.domain.paciente.dto_paciente.PacienteDTOList;
import com.voll.api.domain.paciente.dto_paciente.PacienteDTOModified;
import com.voll.api.domain.paciente.PacienteData;
import com.voll.api.domain.paciente.Paciente;
import com.voll.api.domain.paciente.PacienteService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/pacientes")
@SecurityRequirement(name = "bearer-key") // Para Header de autorización con SpringDocs
public class PacienteController {

    @Autowired
    private PacienteService service;

    @GetMapping
    public ResponseEntity<Page<PacienteDTOList>> listar(@PageableDefault(size=10, sort={"id"}) Pageable pageable) {
        var page = service.listar(pageable);
        return ResponseEntity.ok(page);
    }

    @PostMapping
    public ResponseEntity<PacienteDTO> registro(@RequestBody @Valid PacienteData json, UriComponentsBuilder uriComponentsBuilder) {
        var paciente = service.registrar(json);
        var uri = uriComponentsBuilder
                .path("/pacientes/{id}")
                .buildAndExpand(paciente.getId())
                .toUri();

        return ResponseEntity.created(uri).body(new PacienteDTO(paciente));
    }

    @PutMapping
    public ResponseEntity<PacienteDTO> atualizar(@RequestBody @Valid PacienteDTOModified datos) {
        var paciente = service.actualizar(datos);
        return ResponseEntity.ok(paciente);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PacienteDTO> buscar(@PathVariable Long id) {
        Paciente paciente = service.buscar(id);
        return ResponseEntity.ok(new PacienteDTO(paciente));
    }
}
