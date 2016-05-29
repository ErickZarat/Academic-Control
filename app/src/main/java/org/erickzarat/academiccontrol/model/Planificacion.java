package org.erickzarat.academiccontrol.model;

import java.util.List;

/**
 * Created by erick on 28/05/16.
 */
public class Planificacion {

    private int idPlanificacion;
    private Bimestre bimestre;
    private Profesor profesor;
    private Materia materia;
    private Grado grado;
    private String competencia;
    private List<Actividad> actividades;
}
