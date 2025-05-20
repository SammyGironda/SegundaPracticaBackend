package com.universidad.service;

import com.universidad.dto.InscripcionDTO;
import com.universidad.model.Inscripcion;

import java.util.List;

public interface IInscripcionService {
    List<InscripcionDTO> obtenerTodasLasInscripciones();
    InscripcionDTO obtenerInscripcionPorId(Long id);
    List<InscripcionDTO> obtenerInscripcionesPorEstudiante(Long estudianteId);
    List<InscripcionDTO> obtenerInscripcionesPorMateria(Long materiaId);
    List<InscripcionDTO> obtenerInscripcionesPendientes();
    InscripcionDTO crearInscripcion(Long estudianteId, Long materiaId);
    InscripcionDTO actualizarEstadoInscripcion(Long id, Inscripcion.EstadoInscripcion estado);
    void cancelarInscripcion(Long id);
}