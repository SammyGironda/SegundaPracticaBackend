package com.universidad.controller;

import com.universidad.dto.DocenteDTO;
import com.universidad.service.IDocenteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/docentes")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Docentes", description = "Operaciones relacionadas con los docentes")
public class DocenteController {

    private final IDocenteService docenteService;

    @Autowired
    public DocenteController(IDocenteService docenteService) {
        this.docenteService = docenteService;
    }

    @GetMapping
    @Operation(summary = "Obtener todos los docentes")
    public ResponseEntity<List<DocenteDTO>> obtenerTodosLosDocentes() {
        return ResponseEntity.ok(docenteService.obtenerTodosLosDocentes());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener docente por ID")
    public ResponseEntity<DocenteDTO> obtenerDocentePorId(@PathVariable Long id) {
        DocenteDTO docente = docenteService.obtenerDocentePorId(id);
        return docente != null ? ResponseEntity.ok(docente) : ResponseEntity.notFound().build();
    }

    @GetMapping("/nro-empleado/{nroEmpleado}")
    @Operation(summary = "Obtener docente por número de empleado")
    public ResponseEntity<DocenteDTO> obtenerDocentePorNroEmpleado(@PathVariable String nroEmpleado) {
        DocenteDTO docente = docenteService.obtenerDocentePorNroEmpleado(nroEmpleado);
        return docente != null ? ResponseEntity.ok(docente) : ResponseEntity.notFound().build();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear un nuevo docente")
    public ResponseEntity<DocenteDTO> crearDocente(@RequestBody DocenteDTO docenteDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(docenteService.crearDocente(docenteDTO));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Actualizar docente")
    public ResponseEntity<DocenteDTO> actualizarDocente(@PathVariable Long id, @RequestBody DocenteDTO docenteDTO) {
        return ResponseEntity.ok(docenteService.actualizarDocente(id, docenteDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar docente")
    public ResponseEntity<Void> eliminarDocente(@PathVariable Long id) {
        docenteService.eliminarDocente(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{docenteId}/materias/{materiaId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Asignar materia a docente")
    public ResponseEntity<Void> asignarMateriaADocente(
            @PathVariable Long docenteId,
            @PathVariable Long materiaId) {
        docenteService.asignarMateriaADocente(docenteId, materiaId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{docenteId}/materias/{materiaId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Remover materia de docente")
    public ResponseEntity<Void> removerMateriaDeDocente(
            @PathVariable Long docenteId,
            @PathVariable Long materiaId) {
        docenteService.removerMateriaDeDocente(docenteId, materiaId);
        return ResponseEntity.noContent().build();
    }
}