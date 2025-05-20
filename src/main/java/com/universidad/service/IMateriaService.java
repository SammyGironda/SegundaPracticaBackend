package com.universidad.service;

import com.universidad.dto.MateriaDTO;
import com.universidad.model.Materia;

import java.util.List;

public interface IMateriaService {
    List<MateriaDTO> obtenerTodasLasMaterias();
    MateriaDTO obtenerMateriaPorId(Long id);
    MateriaDTO obtenerMateriaPorCodigoUnico(String codigoUnico);
    MateriaDTO crearMateria(MateriaDTO materiaDTO);
    MateriaDTO actualizarMateria(Long id, MateriaDTO materiaDTO);
    void eliminarMateria(Long id);
    void agregarPrerequisito(Long materiaId, Long prerequisitoId);
    void removerPrerequisito(Long materiaId, Long prerequisitoId);
}