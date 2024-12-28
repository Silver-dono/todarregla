package org.todarregla.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.todarregla.model.Empleado;
import org.todarregla.model.Sector;

import java.util.List;

public interface EmpleadoDAO extends JpaRepository<Empleado, Long> {



    List<Empleado> getEmpleadosBySectorAndActivo(Sector sector, Boolean activo);

    List<Empleado> getEmpleadosBySector(Sector sector);

    @Query(value = "UPDATE Empleado empleado SET empleado.activo = false, empleado.sector = null where empleado.sector.idSector = ?1")
    @Transactional
    @Modifying
    void disableEmpleadosBySector(Long idSector);

}
