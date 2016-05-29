package org.erickzarat.academiccontrol.model;

import java.io.Serializable;

/**
 * Created by erick on 25/05/16.
 */
public class Rol implements Serializable {
    private int idRol;
    private String nombreRol;

    public Rol() {
    }

    public Rol(int idRol, String nombreRol) {
        this.idRol = idRol;
        this.nombreRol = nombreRol;
    }

    public int getIdRol() {
        return idRol;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }

    public String getNombreRol() {
        return nombreRol;
    }

    public void setNombreRol(String nombreRol) {
        this.nombreRol = nombreRol;
    }

    public Rol getAlumno() {
        return new Rol(2, "Alumno");
    }

    public Rol getProfesor() {
        return new Rol(1, "Profesor");
    }
}
