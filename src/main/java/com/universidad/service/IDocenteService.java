package com.universidad.service;

import com.universidad.dto.DocenteDTO;
import com.universidad.model.Docente;

import java.util.List;

public interface IDocenteService {
    List<DocenteDTO> obtenerTodosLosDocentes();
    DocenteDTO obtenerDocentePorId(Long id);
    DocenteDTO obtenerDocentePorNroEmpleado(String nroEmpleado);
    DocenteDTO crearDocente(DocenteDTO docenteDTO);
    DocenteDTO actualizarDocente(Long id, DocenteDTO docenteDTO);
    void eliminarDocente(Long id);
    void asignarMateriaADocente(Long docenteId, Long materiaId);
    void removerMateriaDeDocente(Long docenteId, Long materiaId);
}