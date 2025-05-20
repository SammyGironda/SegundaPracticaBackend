package com.universidad.service.impl;

import com.universidad.dto.DocenteDTO;
import com.universidad.model.Docente;
import com.universidad.model.Materia;
import com.universidad.repository.DocenteRepository;
import com.universidad.repository.MateriaRepository;
import com.universidad.service.IDocenteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DocenteServiceImpl implements IDocenteService {

    private final DocenteRepository docenteRepository;
    private final MateriaRepository materiaRepository;

    @Autowired
    public DocenteServiceImpl(DocenteRepository docenteRepository, MateriaRepository materiaRepository) {
        this.docenteRepository = docenteRepository;
        this.materiaRepository = materiaRepository;
    }

    @Override
    @Cacheable("docentes")
    @Transactional(readOnly = true)
    public List<DocenteDTO> obtenerTodosLosDocentes() {
        return docenteRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "docente", key = "#id")
    @Transactional(readOnly = true)
    public DocenteDTO obtenerDocentePorId(Long id) {
        return docenteRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public DocenteDTO obtenerDocentePorNroEmpleado(String nroEmpleado) {
        return docenteRepository.findByNroEmpleado(nroEmpleado)
                .map(this::convertToDTO)
                .orElse(null);
    }

    @Override
    @CacheEvict(value = {"docentes", "docente"}, allEntries = true)
    @Transactional
    public DocenteDTO crearDocente(DocenteDTO docenteDTO) {
        Docente docente = convertToEntity(docenteDTO);
        docente = docenteRepository.save(docente);
        return convertToDTO(docente);
    }

    @Override
    @CacheEvict(value = {"docentes", "docente"}, key = "#id")
    @Transactional
    public DocenteDTO actualizarDocente(Long id, DocenteDTO docenteDTO) {
        Docente docente = docenteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Docente no encontrado"));
        
        docente.setNombre(docenteDTO.getNombre());
        docente.setApellido(docenteDTO.getApellido());
        docente.setEmail(docenteDTO.getEmail());
        docente.setFechaNacimiento(docenteDTO.getFechaNacimiento());
        docente.setNroEmpleado(docenteDTO.getNroEmpleado());
        docente.setDepartamento(docenteDTO.getDepartamento());
        
        docente = docenteRepository.save(docente);
        return convertToDTO(docente);
    }

    @Override
    @CacheEvict(value = {"docentes", "docente"}, key = "#id")
    @Transactional
    public void eliminarDocente(Long id) {
        docenteRepository.deleteById(id);
    }

    @Override
    @CacheEvict(value = {"docentes", "docente"}, allEntries = true)
    @Transactional
    public void asignarMateriaADocente(Long docenteId, Long materiaId) {
        Docente docente = docenteRepository.findById(docenteId)
                .orElseThrow(() -> new RuntimeException("Docente no encontrado"));
        
        Materia materia = materiaRepository.findById(materiaId)
                .orElseThrow(() -> new RuntimeException("Materia no encontrada"));
        
        docente.getMaterias().add(materia);
        docenteRepository.save(docente);
    }

    @Override
    @CacheEvict(value = {"docentes", "docente"}, allEntries = true)
    @Transactional
    public void removerMateriaDeDocente(Long docenteId, Long materiaId) {
        Docente docente = docenteRepository.findById(docenteId)
                .orElseThrow(() -> new RuntimeException("Docente no encontrado"));
        
        Materia materia = materiaRepository.findById(materiaId)
                .orElseThrow(() -> new RuntimeException("Materia no encontrada"));
        
        docente.getMaterias().remove(materia);
        docenteRepository.save(docente);
    }

    private DocenteDTO convertToDTO(Docente docente) {
        Set<Long> materiasIds = docente.getMaterias().stream()
                .map(Materia::getId)
                .collect(Collectors.toSet());
        
        return DocenteDTO.builder()
                .id(docente.getId())
                .nombre(docente.getNombre())
                .apellido(docente.getApellido())
                .email(docente.getEmail())
                .fechaNacimiento(docente.getFechaNacimiento())
                .nroEmpleado(docente.getNroEmpleado())
                .departamento(docente.getDepartamento())
                .materiasIds(materiasIds)
                .build();
    }

    private Docente convertToEntity(DocenteDTO docenteDTO) {
        return Docente.builder()
                .id(docenteDTO.getId())
                .nombre(docenteDTO.getNombre())
                .apellido(docenteDTO.getApellido())
                .email(docenteDTO.getEmail())
                .fechaNacimiento(docenteDTO.getFechaNacimiento())
                .nroEmpleado(docenteDTO.getNroEmpleado())
                .departamento(docenteDTO.getDepartamento())
                .build();
    }
}