package com.universidad.repository;

import com.universidad.model.Inscripcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {
    List<Inscripcion> findByEstudianteId(Long estudianteId);
    List<Inscripcion> findByMateriaId(Long materiaId);
    boolean existsByEstudianteIdAndMateriaId(Long estudianteId, Long materiaId);
    
    @Query("SELECT i FROM Inscripcion i WHERE i.estado = 'PENDIENTE'")
    List<Inscripcion> findInscripcionesPendientes();
}