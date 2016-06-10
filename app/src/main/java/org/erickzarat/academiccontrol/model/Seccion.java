package org.erickzarat.academiccontrol.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by erick on 28/05/16.
 */
public class Seccion implements Serializable {

    private int idSeccion;
    private String nombreSeccion;
    private List<Alumno> alumnos;

    public Seccion(int idSeccion, String nombreSeccion, List<Alumno> alumnos) {
        this.idSeccion = idSeccion;
        this.nombreSeccion = nombreSeccion;
        this.alumnos = alumnos;
    }

    public Seccion() {

    }

    public Seccion(int idSeccion, String nombreSeccion) {
        this.idSeccion = idSeccion;
        this.nombreSeccion = nombreSeccion;
    }

    public int getIdSeccion() {
        return idSeccion;
    }

    public void setIdSeccion(int idSeccion) {
        this.idSeccion = idSeccion;
    }

    public String getNombreSeccion() {
        return nombreSeccion;
    }

    public void setNombreSeccion(String nombreSeccion) {
        this.nombreSeccion = nombreSeccion;
    }

    public List<Alumno> getAlumnos() {
        return alumnos;
    }

    public void setAlumnos(List<Alumno> alumnos) {
        this.alumnos = alumnos;
    }
}
