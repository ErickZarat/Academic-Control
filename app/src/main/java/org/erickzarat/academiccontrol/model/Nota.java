package org.erickzarat.academiccontrol.model;

import java.io.Serializable;

/**
 * Created by erick on 28/05/16.
 */
public class Nota  implements Serializable {

    private int idNota;
    private int punteo;
    private Actividad actividad;
    private Alumno alumno;

    public Nota() {
    }

    public Nota(int idNota, int punteo, Actividad actividad, Alumno alumno) {
        this.idNota = idNota;
        this.punteo = punteo;
        this.actividad = actividad;
        this.alumno = alumno;
    }

    public int getIdNota() {
        return idNota;
    }

    public void setIdNota(int idNota) {
        this.idNota = idNota;
    }

    public int getPunteo() {
        return punteo;
    }

    public void setPunteo(int punteo) {
        this.punteo = punteo;
    }

    public Actividad getActividad() {
        return actividad;
    }

    public void setActividad(Actividad actividad) {
        this.actividad = actividad;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }
}
