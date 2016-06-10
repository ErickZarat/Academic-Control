package org.erickzarat.academiccontrol.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by erick on 28/05/16.
 */
public class Grado  implements Serializable {
    private int idGrado;
    private String nombreGrado;
    private List<Alumno> alumnos;

    public Grado() {
    }

    public Grado(int idGrado, String nombreGrado) {
        this.idGrado = idGrado;
        this.nombreGrado = nombreGrado;
    }

    public Grado(int idGrado, String nombreGrado, List<Alumno> alumnos) {
        this.idGrado = idGrado;
        this.nombreGrado = nombreGrado;
        this.alumnos = alumnos;
    }

    public List<Alumno> getAlumnos() {
        return alumnos;
    }

    public void setAlumnos(List<Alumno> alumnos) {
        this.alumnos = alumnos;
    }

    public int getIdGrado() {
        return idGrado;
    }

    public void setIdGrado(int idGrado) {
        this.idGrado = idGrado;
    }

    public String getNombreGrado() {
        return nombreGrado;
    }

    public void setNombreGrado(String nombreGrado) {
        this.nombreGrado = nombreGrado;
    }
}
