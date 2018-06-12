package ar.edu.unlp.pasae.tp_integrador.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ar.edu.unlp.pasae.tp_integrador.entities.Pathology;

public interface PathologyRepository extends JpaRepository<Pathology, Long> {
  Optional<Pathology> findByName(String name);
}
