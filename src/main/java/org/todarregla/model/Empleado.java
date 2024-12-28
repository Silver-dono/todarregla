package org.todarregla.model;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;

@Entity
@Table(name = "empleado")
public class Empleado {

    @Id
    @Column(name = "id_empleado", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idEmpleado;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "activo")
    private Boolean activo;

    @Fetch(FetchMode.JOIN)
    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Sector.class)
    @JoinColumn(name = "id_sector", referencedColumnName = "id_sector")
    private Sector sector;

    public Empleado() {
    }

    public Empleado(Long idEmpleado, String nombre, Boolean activo, Sector sector) {
        this.idEmpleado = idEmpleado;
        this.nombre = nombre;
        this.activo = activo;
        this.sector = sector;
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

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Sector getSector() {
        return sector;
    }

    public void setSector(Sector sector) {
        this.sector = sector;
    }
}
