package org.todarregla.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.todarregla.model.Empleado;
import org.todarregla.model.HorariosEmpleados;
import org.todarregla.model.Incidencia;
import org.todarregla.model.Sector;
import org.todarregla.repositories.EmpleadoDAO;
import org.todarregla.repositories.HorariosEmpleadosDAO;
import org.todarregla.repositories.IncidenciaDAO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DateUtils {


    public static String getAvailableHorariosAsString(@Autowired IncidenciaDAO incidenciaDAO, @Autowired EmpleadoDAO empleadoDAO,
                                                      @Autowired HorariosEmpleadosDAO horariosEmpleadosDAO, Sector sector){

        List<Empleado> empleados = empleadoDAO.getEmpleadosBySectorAndActivo(sector, true);
        List<HorariosEmpleados> horarios = horariosEmpleadosDAO.getHorariosEmpleadosByEmpleadoIn(empleados);
        List<Incidencia> incidencias = incidenciaDAO.getIncidenciasByEmpleadoInAndCompletadaAndConfirmada(empleados, false, true);

        Map<Long, List<Incidencia>> incidenciasMap = new HashMap<>();

        incidencias.forEach(
                i -> incidenciasMap.computeIfAbsent(i.getEmpleado().getIdEmpleado(), in -> new ArrayList<>())
                        .add(i));


        return MailUtils.parseHorarios(horarios, incidenciasMap);
    }

}
