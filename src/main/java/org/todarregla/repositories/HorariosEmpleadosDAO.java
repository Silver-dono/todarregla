package org.todarregla.repositories;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.todarregla.model.Empleado;
import org.todarregla.model.Horario;
import org.todarregla.model.HorariosEmpleados;
import org.todarregla.model.Sector;

import java.sql.Time;
import java.util.Collection;
import java.util.List;

public interface HorariosEmpleadosDAO extends JpaRepository<HorariosEmpleados, Long> {

    @Query("SELECT DISTINCT horariosEmpl.horario from HorariosEmpleados horariosEmpl where horariosEmpl.empleado in ?1")
    List<Horario> getAllHorariosByEmpleados(List<Empleado> empleado);

    List<HorariosEmpleados> getHorariosEmpleadosByEmpleadoIn(Collection<Empleado> empleados);

    @Query(value = "SELECT horariosEmpleados.empleado " +
            "from HorariosEmpleados horariosEmpleados " +
            "where horariosEmpleados.horario.idHorario = ?2 and horariosEmpleados.empleado.sector = ?3 " +
            "and horariosEmpleados.empleado not in ?1"
            )
    List<Empleado> getEmpleadosByDateAndSectorAndNotInList_ValidList(List<Empleado> empleados, Long horarioId, Sector sector);

    @Query(value = "SELECT horariosEmpleados.empleado " +
            "from HorariosEmpleados horariosEmpleados " +
            "where horariosEmpleados.horario.idHorario = ?1 and horariosEmpleados.empleado.sector = ?2 "
    )
    List<Empleado> getEmpleadosByDateAndSectorAndNotInList_EmptyValidList(Long horarioId, Sector sector);

    default List<Empleado> getEmpleadosByDateAndSectorAndNotInList(List<Empleado> empleados, Long horarioId, Sector sector){
        if(CollectionUtils.isNotEmpty(empleados)){
            return getEmpleadosByDateAndSectorAndNotInList_ValidList(empleados, horarioId, sector);
        } else {
            return getEmpleadosByDateAndSectorAndNotInList_EmptyValidList(horarioId, sector);
        }
    }


    @Transactional
    void deleteAllByEmpleado_IdEmpleado(Long idEmpleado);


}
