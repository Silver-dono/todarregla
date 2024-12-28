package org.todarregla.model;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;

@Entity
@Table(name = "horarios_empleados")
public class HorariosEmpleados {

    @Id
    @Column(name = "id_horarios_empleados", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idHorariosEmpleados;

    @Fetch(FetchMode.JOIN)
    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Horario.class)
    @JoinColumn(name = "id_horario", referencedColumnName = "id_horario", nullable = false)
    private Horario horario;

    @Fetch(FetchMode.JOIN)
    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Empleado.class)
    @JoinColumn(name = "id_empleado", referencedColumnName = "id_empleado", nullable = false)
    private Empleado empleado;

    public HorariosEmpleados() {
    }

    public HorariosEmpleados(Long idHorariosEmpleados, Horario horario, Empleado empleado) {
        this.idHorariosEmpleados = idHorariosEmpleados;
        this.horario = horario;
        this.empleado = empleado;
    }

    public Long getIdHorariosEmpleados() {
        return idHorariosEmpleados;
    }

    public void setIdHorariosEmpleados(Long idHorariosEmpleados) {
        this.idHorariosEmpleados = idHorariosEmpleados;
    }

    public Horario getHorario() {
        return horario;
    }

    public void setHorario(Horario horario) {
        this.horario = horario;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }
}
