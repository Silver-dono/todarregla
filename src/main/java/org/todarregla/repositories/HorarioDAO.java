package org.todarregla.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.todarregla.model.Horario;

public interface HorarioDAO extends JpaRepository<Horario, Long> {
}
