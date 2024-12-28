package org.todarregla.services.request;

import java.util.List;

public class UpdateEmpleadoRequest {

    private Long idEmpleado;
    private String nombre;
    private Long idSector;
    private Boolean activo;
    private List<Long> idHorarios;

    public UpdateEmpleadoRequest(){

    }

    public UpdateEmpleadoRequest(Long idEmpleado, String nombre, Long idSector, Boolean activo, List<Long> idHorarios) {
        this.idEmpleado = idEmpleado;
        this.nombre = nombre;
        this.idSector = idSector;
        this.activo = activo;
        this.idHorarios = idHorarios;
    }

    public Long getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(Long idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getIdSector() {
        return idSector;
    }

    public void setIdSector(Long idSector) {
        this.idSector = idSector;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public List<Long> getIdHorarios() {
        return idHorarios;
    }

    public void setIdHorarios(List<Long> idHorarios) {
        this.idHorarios = idHorarios;
    }
}
