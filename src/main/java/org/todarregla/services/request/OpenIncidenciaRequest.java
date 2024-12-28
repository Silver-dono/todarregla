package org.todarregla.services.request;

public class OpenIncidenciaRequest {

    private String correo;
    private String calle;
    private Long idSector;

    public OpenIncidenciaRequest() {
    }

    public OpenIncidenciaRequest(String correo, String calle, Long idSector) {
        this.correo = correo;
        this.calle = calle;
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

    public Long getIdSector() {
        return idSector;
    }

    public void setIdSector(Long idSector) {
        this.idSector = idSector;
    }
}
