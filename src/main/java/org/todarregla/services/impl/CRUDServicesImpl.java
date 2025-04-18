package org.todarregla.services.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.todarregla.cache.HorarioCache;
import org.todarregla.cache.SectorCache;
import org.todarregla.model.*;
import org.todarregla.repositories.EmpleadoDAO;
import org.todarregla.repositories.HorariosEmpleadosDAO;
import org.todarregla.repositories.IncidenciaDAO;
import org.todarregla.services.CRUDServices;
import org.todarregla.services.request.UpdateEmpleadoRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class CRUDServicesImpl implements CRUDServices {

    @Autowired
    private SectorCache sectorCache;

    @Autowired
    private EmpleadoDAO empleadoDAO;

    @Autowired
    private HorariosEmpleadosDAO horariosEmpleadosDAO;

    @Autowired
    private HorarioCache horarioCache;

    @Autowired
    private IncidenciaDAO incidenciaDAO;

    @Override
    public String adminPage() {
        return "adminPage";
    }

    @Override
    public String manageSector(Model model) {
        List<Sector> sectores = sectorCache.findAll();
        model.addAttribute("total", CollectionUtils.size(sectores));
        model.addAttribute("sector", sectores);
        return "manageSector";
    }

    @Override
    public String deleteSector(Long idSector, Model model){

        empleadoDAO.disableEmpleadosBySector(idSector);

        sectorCache.deleteById(idSector);
        return manageSector(model);
    }

    @Override
    public String manageEmpleado(Model model) {
        List<Empleado> empleados = empleadoDAO.findAll();
        model.addAttribute("total", CollectionUtils.size(empleados));
        model.addAttribute("empleado", empleados);
        return "manageEmpleado";
    }

    @Override
    public String createEmpleado(Model model) {
        model.addAttribute("sector", sectorCache.findAll());
        return "createEmpleado";
    }

    @Override
    public String newEmpleado(UpdateEmpleadoRequest updateEmpleadoRequest, Model model){
        Empleado empleado = new Empleado();
        generateEmpleado(empleado, updateEmpleadoRequest);

        return manageEmpleado(model);
    }

    @Override
    public String editEmpleado(Long idEmpleado, Model model) {
        Empleado empleado = empleadoDAO.getById(idEmpleado);

        List<Horario> horarios = horariosEmpleadosDAO.getAllHorariosByEmpleados(Collections.singletonList(empleado));

        EditEmpleado editEmpleado = new EditEmpleado(empleado, sectorCache.findAll(), horarios);

        model.addAttribute("empleado", editEmpleado);
        return "editEmpleado";
    }

    @Override
    public String updateEmpleado(UpdateEmpleadoRequest updateEmpleadoRequest, Model model){

        Empleado newEmpleado = new Empleado();
        newEmpleado.setIdEmpleado(updateEmpleadoRequest.getIdEmpleado());

        generateEmpleado(newEmpleado, updateEmpleadoRequest);


        return manageEmpleado(model);
    }

    private void generateEmpleado(Empleado empleado, UpdateEmpleadoRequest updateEmpleadoRequest){
        empleado.setNombre(updateEmpleadoRequest.getNombre());
        empleado.setActivo(updateEmpleadoRequest.getActivo());
        empleado.setSector(sectorCache.findById(updateEmpleadoRequest.getIdSector()));

        empleadoDAO.save(empleado);

        //Update HorariosEmpleados table
        horariosEmpleadosDAO.deleteAllByEmpleado_IdEmpleado(empleado.getIdEmpleado());
        for(Long idHorario : updateEmpleadoRequest.getIdHorarios()){
            HorariosEmpleados horariosEmpleados = new HorariosEmpleados();
            horariosEmpleados.setEmpleado(empleado);
            horariosEmpleados.setHorario(horarioCache.findById(idHorario));
            horariosEmpleadosDAO.save(horariosEmpleados);
        }

    }

    @Override
    public String manageIncidencia(Model model) {
        List<Incidencia> incidencias = incidenciaDAO.getIncidenciasByCompletada(false);
        model.addAttribute("total", CollectionUtils.size(incidencias));
        model.addAttribute("incidencia", incidencias);
        return "manageIncidencia";
    }

    @Override
    public String closeIncidencia(Long idIncidencia, Model model) {
        incidenciaDAO.updateCompletadaField(idIncidencia);
        return manageIncidencia(model);
    }

    //Wrapper class to handle properly editEmpleado template
    private class EditEmpleado {
        private final Long idEmpleado;
        private final String nombre;
        private final Boolean activo;
        private final List<Sector> sectores = new ArrayList<>();
        private final List<Long> horarios = new ArrayList<>();

        public EditEmpleado(Empleado empleado, List<Sector> sectores, List<Horario> horarios){
            this.idEmpleado = empleado.getIdEmpleado();
            this.nombre = empleado.getNombre();
            this.activo = empleado.getActivo();


            if(empleado.getSector() != null)
                this.sectores.add(empleado.getSector());

            sectores.forEach(s -> {
                if (!this.sectores.contains(s))
                    this.sectores.add(s);
            });

            this.horarios.addAll(horarios.stream().map(Horario::getIdHorario).collect(Collectors.toList()));
        }

        public Long getIdEmpleado() {
            return idEmpleado;
        }

        public String getNombre() {
            return nombre;
        }

        public Boolean getActivo() {
            return activo;
        }

        public List<Sector> getSectores() {
            return sectores;
        }
    }

}
