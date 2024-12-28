package org.todarregla.model;

import javax.persistence.*;

@Entity
@Table(name = "sector")
public class Sector {

    @Id
    @Column(name = "id_sector", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idSector;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    public Sector() {
    }

    public Sector(Long idSector, String nombre) {
        this.idSector = idSector;
        this.nombre = nombre;
    }

    public Long getIdSector() {
        return idSector;
    }

    public void setIdSector(Long idSector) {
        this.idSector = idSector;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
