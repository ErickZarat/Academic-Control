package org.erickzarat.academiccontrol.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by erick on 28/05/16.
 */
public class Planificacion implements Serializable {

    private int idPlanificacion;
    private Bimestre bimestre;
    private Profesor profesor;
    private Materia materia;
    private Grado grado;
    private String competencia;
    private List<Actividad> actividades;

    public Planificacion() {
    }

    public Planificacion(int idPlanificacion, Bimestre bimestre, Profesor profesor, Materia materia, Grado grado, String competencia) {
        this.idPlanificacion = idPlanificacion;
        this.bimestre = bimestre;
        this.profesor = profesor;
        this.materia = materia;
        this.grado = grado;
        this.competencia = competencia;
    }

    public int getIdPlanificacion() {
        return idPlanificacion;
    }

    public void setIdPlanificacion(int idPlanificacion) {
        this.idPlanificacion = idPlanificacion;
    }

    public Bimestre getBimestre() {
        return bimestre;
    }

    public void setBimestre(Bimestre bimestre) {
        this.bimestre = bimestre;
    }

    public Profesor getProfesor() {
        return profesor;
    }

    public void setProfesor(Profesor profesor) {
        this.profesor = profesor;
    }

    public Materia getMateria() {
        return materia;
    }

    public void setMateria(Materia materia) {
        this.materia = materia;
    }

    public Grado getGrado() {
        return grado;
    }

    public void setGrado(Grado grado) {
        this.grado = grado;
    }

    public String getCompetencia() {
        return competencia;
    }

    public void setCompetencia(String competencia) {
        this.competencia = competencia;
    }

    public List<Actividad> getActividades() {
        return actividades;
    }

    public void setActividades(List<Actividad> actividades) {
        this.actividades = actividades;
    }
}
