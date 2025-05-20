package com.universidad.dto;

import com.universidad.model.Inscripcion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InscripcionDTO {
    private Long id;
    private LocalDateTime fechaInscripcion;
    private Long estudianteId;
    private Long materiaId;
    private Inscripcion.EstadoInscripcion estado;
    private String nombreEstudiante;
    private String nombreMateria;
}