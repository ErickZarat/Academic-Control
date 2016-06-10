package org.erickzarat.academiccontrol.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by erick on 28/05/16.
 */
public class Alumno extends Usuario implements Serializable {

    private Grado grado;
    private Seccion seccion;
    private List<Materia> materias;
    private List<Nota> notas;

    public Alumno() {
        super();
    }

    public Alumno(int idUsuario, String nombre, String apellido, String nick, String contrasena, Rol rol, Grado grado, Seccion seccion, List<Materia> materias, List<Nota> notas) {
        super(idUsuario, nombre, apellido, nick, contrasena, rol);
        this.grado = grado;
        this.seccion = seccion;
        this.materias = materias;
        this.notas = notas;
    }

    public List<Materia> getMaterias() {
        return materias;
    }

    public void setMaterias(List<Materia> materias) {
        this.materias = materias;
    }

    public Grado getGrado() {
        return grado;
    }

    public void setGrado(Grado grado) {
        this.grado = grado;
    }

    public Seccion getSeccion() {
        return seccion;
    }

    public void setSeccion(Seccion seccion) {
        this.seccion = seccion;
    }
}
