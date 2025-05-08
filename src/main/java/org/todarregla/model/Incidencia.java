package org.todarregla.model;

import org.apache.commons.lang3.builder.ToStringExclude;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "incidencia")
public class Incidencia {

    @Id
    @Column(name = "id_incidencia", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idIncidencia;

    @Column(name = "correo", nullable = false)
    private String correo;

     @Column(name = "nombre_cliente")
    private String nombreCliente;

    @Column(name = "fecha")
    private Date fecha;

    @Column(name = "calle")
    private String calle;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "confirmada")
    private Boolean confirmada = false;

    @Column(name = "completada")
    private Boolean completada = false;

    @Fetch(FetchMode.JOIN)
    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Empleado.class)
    @JoinColumn(name = "id_empleado", referencedColumnName = "id_empleado")
    private Empleado empleado;

    @Fetch(FetchMode.JOIN)
    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Sector.class)
    @JoinColumn(name = "id_sector", referencedColumnName = "id_sector", nullable = false)
    private Sector sector;

    public Incidencia() {
    }

    public Incidencia(Long idIncidencia, String correo, String nombreCliente, Date fecha, String calle, String descripcion, Boolean confirmada, Boolean completada, Empleado empleado, Sector sector) {
        this.idIncidencia = idIncidencia;
        this.correo = correo;
        this.nombreCliente = nombreCliente;
        this.fecha = fecha;
        this.calle = calle;
        this.descripcion = descripcion;
        this.confirmada = confirmada;
        this.completada = completada;
        this.empleado = empleado;
        this.sector = sector;
    }

    public Long getIdIncidencia() {
        return idIncidencia;
    }

    public void setIdIncidencia(Long idIncidencia) {
        this.idIncidencia = idIncidencia;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Boolean getConfirmada() {
        return confirmada;
    }

    public void setConfirmada(Boolean confirmada) {
        this.confirmada = confirmada;
    }

    public Boolean getCompletada() {
        return completada;
    }

    public void setCompletada(Boolean completada) {
        this.completada = completada;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public Sector getSector() {
        return sector;
    }

    public void setSector(Sector sector) {
        this.sector = sector;
    }
}
