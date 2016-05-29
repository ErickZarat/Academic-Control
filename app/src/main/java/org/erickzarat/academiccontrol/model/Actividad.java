package org.erickzarat.academiccontrol.model;

import java.util.Date;
import java.util.List;

/**
 * Created by erick on 28/05/16.
 */
public class Actividad {

    private int idActividad;
    private String contenido;
    private Date fechaInicial;
    private Date fechaFinal;
    private String materiales;
    private String tareas;
    private int ponderacion;
    private String logro;
    private Planificacion planificacion;
    private List<Nota> notas;

    public Actividad() {
    }

    public Actividad(int idActividad, String contenido, Date fechaInicial, Date fechaFinal, String materiales, String tareas, int ponderacion, String logro, Planificacion planificacion, List<Nota> notas) {
        this.idActividad = idActividad;
        this.contenido = contenido;
        this.fechaInicial = fechaInicial;
        this.fechaFinal = fechaFinal;
        this.materiales = materiales;
        this.tareas = tareas;
        this.ponderacion = ponderacion;
        this.logro = logro;
        this.planificacion = planificacion;
        this.notas = notas;
    }

    public List<Nota> getNotas() {
        return notas;
    }

    public void setNotas(List<Nota> notas) {
        this.notas = notas;
    }

    public int getIdActividad() {
        return idActividad;
    }

    public void setIdActividad(int idActividad) {
        this.idActividad = idActividad;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public Date getFechaInicial() {
        return fechaInicial;
    }

    public void setFechaInicial(Date fechaInicial) {
        this.fechaInicial = fechaInicial;
    }

    public Date getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(Date fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public String getMateriales() {
        return materiales;
    }

    public void setMateriales(String materiales) {
        this.materiales = materiales;
    }

    public String getTareas() {
        return tareas;
    }

    public void setTareas(String tareas) {
        this.tareas = tareas;
    }

    public int getPonderacion() {
        return ponderacion;
    }

    public void setPonderacion(int ponderacion) {
        this.ponderacion = ponderacion;
    }

    public String getLogro() {
        return logro;
    }

    public void setLogro(String logro) {
        this.logro = logro;
    }

    public Planificacion getPlanificacion() {
        return planificacion;
    }

    public void setPlanificacion(Planificacion planificacion) {
        this.planificacion = planificacion;
    }
}
