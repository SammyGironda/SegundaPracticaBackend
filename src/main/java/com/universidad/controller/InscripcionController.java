package com.universidad.controller;

import com.universidad.dto.InscripcionDTO;
import com.universidad.model.Inscripcion;
import com.universidad.service.IInscripcionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inscripciones")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Inscripciones", description = "Operaciones relacionadas con las inscripciones")
public class InscripcionController {

    private final IInscripcionService inscripcionService;

    @Autowired
    public InscripcionController(IInscripcionService inscripcionService) {
        this.inscripcionService = inscripcionService;
    }

    @GetMapping
    @Operation(summary = "Obtener todas las inscripciones")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<InscripcionDTO>> obtenerTodasLasInscripciones() {
        return ResponseEntity.ok(inscripcionService.obtenerTodasLasInscripciones());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener inscripción por ID")
    public ResponseEntity<InscripcionDTO> obtenerInscripcionPorId(@PathVariable Long id) {
        InscripcionDTO inscripcion = inscripcionService.obtenerInscripcionPorId(id);
        return inscripcion != null ? ResponseEntity.ok(inscripcion) : ResponseEntity.notFound().build();
    }

    @GetMapping("/estudiante/{estudianteId}")
    @Operation(summary = "Obtener inscripciones por estudiante")
    public ResponseEntity<List<InscripcionDTO>> obtenerInscripcionesPorEstudiante(
            @PathVariable Long estudianteId) {
        return ResponseEntity.ok(inscripcionService.obtenerInscripcionesPorEstudiante(estudianteId));
    }

    @GetMapping("/materia/{materiaId}")
    @Operation(summary = "Obtener inscripciones por materia")
    public ResponseEntity<List<InscripcionDTO>> obtenerInscripcionesPorMateria(
            @PathVariable Long materiaId) {
        return ResponseEntity.ok(inscripcionService.obtenerInscripcionesPorMateria(materiaId));
    }

    @GetMapping("/pendientes")
    @Operation(summary = "Obtener inscripciones pendientes")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<InscripcionDTO>> obtenerInscripcionesPendientes() {
        return ResponseEntity.ok(inscripcionService.obtenerInscripcionesPendientes());
    }

    @PostMapping
    @Operation(summary = "Crear nueva inscripción")
    public ResponseEntity<InscripcionDTO> crearInscripcion(
            @RequestParam Long estudianteId,
            @RequestParam Long materiaId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(inscripcionService.crearInscripcion(estudianteId, materiaId));
    }

    @PutMapping("/{id}/estado")
    @Operation(summary = "Actualizar estado de inscripción")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InscripcionDTO> actualizarEstadoInscripcion(
            @PathVariable Long id,
            @RequestParam Inscripcion.EstadoInscripcion estado) {
        return ResponseEntity.ok(inscripcionService.actualizarEstadoInscripcion(id, estado));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Cancelar inscripción")
    public ResponseEntity<Void> cancelarInscripcion(@PathVariable Long id) {
        inscripcionService.cancelarInscripcion(id);
        return ResponseEntity.noContent().build();
    }
}