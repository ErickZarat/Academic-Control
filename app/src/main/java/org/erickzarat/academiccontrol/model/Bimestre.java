package org.erickzarat.academiccontrol.model;

/**
 * Created by erick on 28/05/16.
 */
public class Bimestre {

    private int idBimestre;
    private String nombreBimestre;

    public Bimestre() {
    }

    public Bimestre(int idBimestre, String nombreBimestre) {

        this.idBimestre = idBimestre;
        this.nombreBimestre = nombreBimestre;
    }

    public int getIdBimestre() {
        return idBimestre;
    }

    public void setIdBimestre(int idBimestre) {
        this.idBimestre = idBimestre;
    }

    public String getNombreBimestre() {
        return nombreBimestre;
    }

    public void setNombreBimestre(String nombreBimestre) {
        this.nombreBimestre = nombreBimestre;
    }
}
