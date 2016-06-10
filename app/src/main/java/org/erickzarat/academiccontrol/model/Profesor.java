package org.erickzarat.academiccontrol.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by erick on 28/05/16.
 */
public class Profesor extends Usuario implements Serializable {

    private List<Materia> materias;

    public List<Planificacion> getPlanificaciones() {
        return planificaciones;
    }

    public void setPlanificaciones(List<Planificacion> planificaciones) {
        this.planificaciones = planificaciones;
    }

    public List<Materia> getMaterias() {
        return materias;
    }

    public void setMaterias(List<Materia> materias) {
        this.materias = materias;
    }

    private List<Planificacion> planificaciones;

    public Profesor(int idUsuario, String nombre, String apellido, String nick, String contrasena, Rol rol) {
        super(idUsuario, nombre, apellido, nick, contrasena, rol);
    }

    public Profesor() {
    }
}
