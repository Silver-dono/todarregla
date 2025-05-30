package org.todarregla.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.todarregla.model.Sector;

public interface SectorDAO extends JpaRepository<Sector, Long> {
}
