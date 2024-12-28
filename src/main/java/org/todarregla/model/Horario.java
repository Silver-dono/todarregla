package org.todarregla.model;

import javax.persistence.*;
import java.sql.Time;

@Entity
@Table(name = "horario")
public class Horario {

    @Id
    @Column(name = "id_horario", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idHorario;

    @Column(name = "hora_inicial", nullable = false)
    private Time horaInicio;

    @Column(name = "hora_final", nullable = false)
    private Time horaFinal;

    public Horario() {
    }

    public Horario(Long idHorario, Time horaInicio, Time horaFinal) {
        this.idHorario = idHorario;
        this.horaInicio = horaInicio;
        this.horaFinal = horaFinal;
    }

    public Long getIdHorario() {
        return idHorario;
    }

    public void setIdHorario(Long idHorario) {
        this.idHorario = idHorario;
    }

    public Time getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(Time horaInicio) {
        this.horaInicio = horaInicio;
    }

    public Time getHoraFinal() {
        return horaFinal;
    }

    public void setHoraFinal(Time horaFinal) {
        this.horaFinal = horaFinal;
    }


    @Override
    public String toString(){
        return horaInicio.toString() + " to " + horaFinal.toString();
    }
}
