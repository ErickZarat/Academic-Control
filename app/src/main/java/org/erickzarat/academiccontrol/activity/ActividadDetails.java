package org.erickzarat.academiccontrol.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.sergiocasero.revealfab.RevealFAB;
import org.erickzarat.academiccontrol.R;
import org.erickzarat.academiccontrol.adapter.GradeRecycleAdapter;
import org.erickzarat.academiccontrol.app.Aplication;
import org.erickzarat.academiccontrol.helper.WebServiceHelper;
import org.erickzarat.academiccontrol.interfaces.OnGradeClickListener;
import org.erickzarat.academiccontrol.interfaces.OnStudentClickListener;
import org.erickzarat.academiccontrol.model.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

public class ActividadDetails extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, OnGradeClickListener {

    @Bind(R.id.appbar)
    Toolbar appbar;
    @Bind(R.id.txtTarea)
    TextView txtTarea;
    @Bind(R.id.txtFecha)
    TextView txtFecha;
    @Bind(R.id.txtMateriales)
    TextView txtMateriales;
    @Bind(R.id.txtPonderacion)
    TextView txtPonderacion;
    @Bind(R.id.txtLogro)
    TextView txtLogro;
    @Bind(R.id.recycle_actividades)
    RecyclerView recycleActividades;
    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    private RevealFAB fabCalificar;

    private final String TAG = ActividadDetails.class.getSimpleName();
    private String URL = null;

    private GradeRecycleAdapter adapter;
    private List<Nota> notas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_details);
        ButterKnife.bind(this);
        setSupportActionBar(appbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        appbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if (Build.VERSION.SDK_INT > 21) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        appbar.setSubtitle("Notas");
        Actividad actividad = (Actividad) getIntent().getSerializableExtra("ACTIVIDAD");

        txtFecha.setText(actividad.getFechaInicial().toString());
        txtLogro.setText(actividad.getLogro());
        txtMateriales.setText(actividad.getMateriales());
        txtTarea.setText(actividad.getTareas());
        txtPonderacion.setText(String.valueOf(actividad.getPonderacion()));

        URL = WebServiceHelper.getInstance(getApplicationContext()).ROUTE_NOTA + "/actividad/"+ actividad.getIdActividad();
        notas = new ArrayList<Nota>();
        initAdapter();
        initRecyclerView();

        fabCalificar = (RevealFAB) findViewById(R.id.fabCalificar);
        Intent i = new Intent(ActividadDetails.this, Calificar.class);
        i.putExtra("ACTIVIDAD", actividad);
        fabCalificar.setIntent(i);

        fabCalificar.setOnClickListener(new RevealFAB.OnClickListener() {
            @Override
            public void onClick(RevealFAB button, View v) {
                button.startActivityWithAnimation();
            }
        });
    }

    private void initRecyclerView() {
        recycleActividades.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recycleActividades.setAdapter(adapter);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent);

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                adapter.clear();
                getGrades();
                adapter.update(notas);
            }
        });
    }

    private void initAdapter() {
        if (adapter == null){
            adapter = new GradeRecycleAdapter(getApplicationContext(), notas);
        }
    }

    private void getGrades(){
        swipeRefreshLayout.setRefreshing(true);
        JsonArrayRequest peticion = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                ArrayList<Nota> temp = new ArrayList<Nota>();
                if (response.length() > 0) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject notaJson = response.getJSONObject(i);

                            int idNota = notaJson.getInt("idNota");
                            int punteo = notaJson.getInt("punteo");

                            java.sql.Date sqldate = java.sql.Date.valueOf(notaJson.getString("fechaInicial").substring(0,9));
                            Actividad actividad = new Actividad(
                                    notaJson.getInt("idActividad"),
                                    notaJson.getString("contenido"),
                                    new Date(sqldate.getTime()),
                                    new Date(sqldate.getTime()),
                                    notaJson.getString("materiales"),
                                    notaJson.getString("tareas"),
                                    notaJson.getInt("ponderacion"),
                                    notaJson.getString("logro"),
                                    null
                            );

                            Alumno persona = new Alumno();
                            persona.setIdUsuario(notaJson.getInt("idUsuario"));
                            persona.setNombre(notaJson.getString("nombre"));
                            persona.setApellido(notaJson.getString("apellido"));
                            persona.setNick(notaJson.getString("nick"));

                            Nota newNota = new Nota();
                            newNota.setIdNota(idNota);
                            newNota.setPunteo(punteo);
                            newNota.setActividad(actividad);
                            newNota.setAlumno(persona);

                            temp.add(0, newNota);
                            Log.e("Punteo ==========",String.valueOf(newNota.getPunteo()));
                        } catch (JSONException ex) {
                            Log.e(TAG, "Json parsing error: " + ex.getMessage());
                        }//end try catch
                    }// end for
                    notas.clear();
                    adapter.update(temp);
                    notas = temp;
                }else {
                    Toast.makeText(getApplicationContext(), "No existen registros de notas", Toast.LENGTH_SHORT).show();
                }
                swipeRefreshLayout.setRefreshing(false);
                //end on response ();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        Aplication.getInstance(getApplicationContext()).addToRequestQueue(peticion);

    }

    @Override
    public void onRefresh() {
        getGrades();
    }

    @Override
    public void onGradeClickListener(Grado grado) {

    }

    public void agregar() {
        final boolean requestOk = false;
        Map<String, String> usr = new HashMap<>();
        usr.put("idNota", null);
       // usr.put("idUsuario", txtCompetencia.getText().toString());
        //usr.put("punteo", String.valueOf(idBim));
        //usr.put("idActividad", String.valueOf(idMat));

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                WebServiceHelper.getInstance(this).ROUTE_PLANIFICACION, new JSONObject(usr),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Toast.makeText(getApplicationContext(), "Mensaje: " + response.getString("mensaje"),
                                    Toast.LENGTH_SHORT).show();
                        } catch (Exception ex) {
                            Log.e("Request Exception", ex.getMessage());
                            Toast.makeText(getApplicationContext(), "Eception", Toast.LENGTH_SHORT).show();
                        }
                        onBackPressed();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error: Response ", error.getMessage());
                Toast.makeText(getApplicationContext(), "Error Volley", Toast.LENGTH_SHORT).show();
            }
        });
        WebServiceHelper.getInstance(this).addToRequestQueue(request);
    }

    @Override
    protected void onResume() {
        super.onResume();
        fabCalificar.onResume();
        getGrades();
    }
}
