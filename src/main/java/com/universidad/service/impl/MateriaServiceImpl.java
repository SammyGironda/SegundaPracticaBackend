package com.universidad.service.impl;

import com.universidad.dto.MateriaDTO;
import com.universidad.model.Materia;
import com.universidad.repository.MateriaRepository;
import com.universidad.service.IMateriaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MateriaServiceImpl implements IMateriaService {

    private final MateriaRepository materiaRepository;

    @Autowired
    public MateriaServiceImpl(MateriaRepository materiaRepository) {
        this.materiaRepository = materiaRepository;
    }

    @Override
    @Cacheable("materias")
    @Transactional(readOnly = true)
    public List<MateriaDTO> obtenerTodasLasMaterias() {
        return materiaRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "materia", key = "#id")
    @Transactional(readOnly = true)
    public MateriaDTO obtenerMateriaPorId(Long id) {
        return materiaRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public MateriaDTO obtenerMateriaPorCodigoUnico(String codigoUnico) {
        Materia materia = materiaRepository.findByCodigoUnico(codigoUnico);
        return materia != null ? convertToDTO(materia) : null;

    }

    @Override
    @CacheEvict(value = {"materias", "materia"}, allEntries = true)
    @Transactional
    public MateriaDTO crearMateria(MateriaDTO materiaDTO) {
        Materia materia = convertToEntity(materiaDTO);
        materia = materiaRepository.save(materia);
        return convertToDTO(materia);
    }

    @Override
    @CacheEvict(value = {"materias", "materia"}, key = "#id")
    @Transactional
    public MateriaDTO actualizarMateria(Long id, MateriaDTO materiaDTO) {
        Materia materia = materiaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Materia no encontrada"));
        
        materia.setNombreMateria(materiaDTO.getNombreMateria());
        materia.setCodigoUnico(materiaDTO.getCodigoUnico());
        materia.setCreditos(materiaDTO.getCreditos());
        
        materia = materiaRepository.save(materia);
        return convertToDTO(materia);
    }

    @Override
    @CacheEvict(value = {"materias", "materia"}, key = "#id")
    @Transactional
    public void eliminarMateria(Long id) {
        materiaRepository.deleteById(id);
    }

    @Override
    @CacheEvict(value = {"materias", "materia"}, allEntries = true)
    @Transactional
    public void agregarPrerequisito(Long materiaId, Long prerequisitoId) {
        Materia materia = materiaRepository.findById(materiaId)
                .orElseThrow(() -> new RuntimeException("Materia no encontrada"));
        
        Materia prerequisito = materiaRepository.findById(prerequisitoId)
                .orElseThrow(() -> new RuntimeException("Prerequisito no encontrado"));
        
        if (materia.formariaCirculo(prerequisitoId)) {
            throw new RuntimeException("No se puede agregar el prerequisito porque formaría un ciclo");
        }
        
        materia.getPrerequisitos().add(prerequisito);
        materiaRepository.save(materia);
    }

    @Override
    @CacheEvict(value = {"materias", "materia"}, allEntries = true)
    @Transactional
    public void removerPrerequisito(Long materiaId, Long prerequisitoId) {
        Materia materia = materiaRepository.findById(materiaId)
                .orElseThrow(() -> new RuntimeException("Materia no encontrada"));
        
        Materia prerequisito = materiaRepository.findById(prerequisitoId)
                .orElseThrow(() -> new RuntimeException("Prerequisito no encontrado"));
        
        materia.getPrerequisitos().remove(prerequisito);
        materiaRepository.save(materia);
    }

    private MateriaDTO convertToDTO(Materia materia) {
        return MateriaDTO.builder()
                .id(materia.getId())
                .nombreMateria(materia.getNombreMateria())
                .codigoUnico(materia.getCodigoUnico())
                .creditos(materia.getCreditos())
                .build();
    }

    private Materia convertToEntity(MateriaDTO materiaDTO) {
        return Materia.builder()
                .id(materiaDTO.getId())
                .nombreMateria(materiaDTO.getNombreMateria())
                .codigoUnico(materiaDTO.getCodigoUnico())
                .creditos(materiaDTO.getCreditos())
                .build();
    }
}