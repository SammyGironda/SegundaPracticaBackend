package com.universidad.service.impl;

import com.universidad.dto.InscripcionDTO;
import com.universidad.model.*;
import com.universidad.repository.EstudianteRepository;
import com.universidad.repository.InscripcionRepository;
import com.universidad.repository.MateriaRepository;
import com.universidad.service.IInscripcionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InscripcionServiceImpl implements IInscripcionService {

    private final InscripcionRepository inscripcionRepository;
    private final EstudianteRepository estudianteRepository;
    private final MateriaRepository materiaRepository;

    @Autowired
    public InscripcionServiceImpl(InscripcionRepository inscripcionRepository,
                                 EstudianteRepository estudianteRepository,
                                 MateriaRepository materiaRepository) {
        this.inscripcionRepository = inscripcionRepository;
        this.estudianteRepository = estudianteRepository;
        this.materiaRepository = materiaRepository;
    }

    @Override
    @Cacheable("inscripciones")
    @Transactional(readOnly = true)
    public List<InscripcionDTO> obtenerTodasLasInscripciones() {
        return inscripcionRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "inscripcion", key = "#id")
    @Transactional(readOnly = true)
    public InscripcionDTO obtenerInscripcionPorId(Long id) {
        return inscripcionRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InscripcionDTO> obtenerInscripcionesPorEstudiante(Long estudianteId) {
        return inscripcionRepository.findByEstudianteId(estudianteId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<InscripcionDTO> obtenerInscripcionesPorMateria(Long materiaId) {
        return inscripcionRepository.findByMateriaId(materiaId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<InscripcionDTO> obtenerInscripcionesPendientes() {
        return inscripcionRepository.findInscripcionesPendientes().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @CacheEvict(value = {"inscripciones", "inscripcion"}, allEntries = true)
    @Transactional
    public InscripcionDTO crearInscripcion(Long estudianteId, Long materiaId) {
        if (inscripcionRepository.existsByEstudianteIdAndMateriaId(estudianteId, materiaId)) {
            throw new RuntimeException("El estudiante ya está inscrito en esta materia");
        }

        Estudiante estudiante = estudianteRepository.findById(estudianteId)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));
        
        Materia materia = materiaRepository.findById(materiaId)
                .orElseThrow(() -> new RuntimeException("Materia no encontrada"));

        Inscripcion inscripcion = Inscripcion.builder()
                .estudiante(estudiante)
                .materia(materia)
                .fechaInscripcion(LocalDateTime.now())
                .estado(Inscripcion.EstadoInscripcion.PENDIENTE)
                .build();

        inscripcion = inscripcionRepository.save(inscripcion);
        return convertToDTO(inscripcion);
    }

    @Override
    @CacheEvict(value = {"inscripciones", "inscripcion"}, key = "#id")
    @Transactional
    public InscripcionDTO actualizarEstadoInscripcion(Long id, Inscripcion.EstadoInscripcion estado) {
        Inscripcion inscripcion = inscripcionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inscripción no encontrada"));
        
        inscripcion.setEstado(estado);
        inscripcion = inscripcionRepository.save(inscripcion);
        return convertToDTO(inscripcion);
    }

    @Override
    @CacheEvict(value = {"inscripciones", "inscripcion"}, key = "#id")
    @Transactional
    public void cancelarInscripcion(Long id) {
        Inscripcion inscripcion = inscripcionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inscripción no encontrada"));
        
        inscripcion.setEstado(Inscripcion.EstadoInscripcion.CANCELADA);
        inscripcionRepository.save(inscripcion);
    }

    private InscripcionDTO convertToDTO(Inscripcion inscripcion) {
        return InscripcionDTO.builder()
                .id(inscripcion.getId())
                .fechaInscripcion(inscripcion.getFechaInscripcion())
                .estudianteId(inscripcion.getEstudiante().getId())
                .materiaId(inscripcion.getMateria().getId())
                .estado(inscripcion.getEstado())
                .nombreEstudiante(inscripcion.getEstudiante().getNombre() + " " + inscripcion.getEstudiante().getApellido())
                .nombreMateria(inscripcion.getMateria().getNombreMateria())
                .build();
    }
}