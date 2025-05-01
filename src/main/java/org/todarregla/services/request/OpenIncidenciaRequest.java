package org.todarregla.services.request;

public class OpenIncidenciaRequest {

    private String correo;
    private String calle;
    private String nombre;
    private String descripcion;
    private String telefono;
    private Long idSector;

    public OpenIncidenciaRequest() {
    }

    public OpenIncidenciaRequest(String correo, String calle, String nombre, String descripcion, String telefono, Long idSector) {
        this.correo = correo;
        this.calle = calle;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.telefono = telefono;
        this.idSector = idSector;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public Long getIdSector() {
        return idSector;
    }

    public void setIdSector(Long idSector) {
        this.idSector = idSector;
    }
}
