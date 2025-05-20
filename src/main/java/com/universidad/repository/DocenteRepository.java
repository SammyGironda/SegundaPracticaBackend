package com.universidad.repository;

import com.universidad.model.Docente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;


public interface DocenteRepository extends JpaRepository<Docente, Long> {
   Optional<Docente> findByNroEmpleado(String nroEmpleado);
}
