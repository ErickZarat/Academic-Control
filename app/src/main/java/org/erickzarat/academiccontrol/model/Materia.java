package org.erickzarat.academiccontrol.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by erick on 28/05/16.
 */
public class Materia  implements Serializable {
    private int idMateria;
    private String nombreMateria;
    private List<Profesor> profesores;

    public Materia() {
    }

    public Materia(int idMateria, String nombreMateria) {
        this.idMateria = idMateria;
        this.nombreMateria = nombreMateria;
    }

    public Materia(int idMateria, String nombreMateria, List<Profesor> profesores) {
        this.idMateria = idMateria;
        this.nombreMateria = nombreMateria;
        this.profesores = profesores;
    }

    public int getIdMateria() {
        return idMateria;
    }

    public void setIdMateria(int idMateria) {
        this.idMateria = idMateria;
    }

    public String getNombreMateria() {
        return nombreMateria;
    }

    public void setNombreMateria(String nombreMateria) {
        this.nombreMateria = nombreMateria;
    }

    public List<Profesor> getProfesores() {
        return profesores;
    }

    public void setProfesores(List<Profesor> profesores) {
        this.profesores = profesores;
    }
}
