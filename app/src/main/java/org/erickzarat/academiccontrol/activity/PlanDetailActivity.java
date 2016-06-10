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
import butterknife.OnClick;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.sergiocasero.revealfab.RevealFAB;
import org.erickzarat.academiccontrol.R;
import org.erickzarat.academiccontrol.adapter.ActivityRecycleAdapter;
import org.erickzarat.academiccontrol.app.Aplication;
import org.erickzarat.academiccontrol.helper.WebServiceHelper;
import org.erickzarat.academiccontrol.interfaces.OnActivityClickListener;
import org.erickzarat.academiccontrol.model.Actividad;
import org.erickzarat.academiccontrol.model.Planificacion;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.spec.ECField;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PlanDetailActivity extends AppCompatActivity implements OnActivityClickListener, SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.appbar)
    Toolbar appbar;
    @Bind(R.id.txtTarea)
    TextView txtCompetencia;
    @Bind(R.id.txtProfesor)
    TextView txtProfesor;
    @Bind(R.id.txtMateriales)
    TextView txtMateria;
    @Bind(R.id.txtPonderacion)
    TextView txtGrado;
    @Bind(R.id.recycle_actividades)
    RecyclerView recycleActividades;
    @Bind(R.id.txtLogro)
    TextView txtBimestre;
    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    RevealFAB fabAddActivity;

    private String TAG = PlanDetailActivity.class.getSimpleName();
    private String URL = null;
    private ActivityRecycleAdapter adapter;
    private List<Actividad> actividades;
    private Planificacion planificacion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_detail);
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

        this.planificacion = (Planificacion) getIntent().getSerializableExtra("PLAN");
        URL = WebServiceHelper.getInstance(getApplicationContext()).ROUTE_ACTIVIDAD + "/planificacion/" + planificacion.getIdPlanificacion();
        txtBimestre.setText(planificacion.getBimestre().getNombreBimestre());
        txtCompetencia.setText(planificacion.getCompetencia());
        txtGrado.setText(planificacion.getGrado().getNombreGrado());
        txtMateria.setText(planificacion.getMateria().getNombreMateria());
        txtProfesor.setText(planificacion.getProfesor().getNombre() + " " + planificacion.getProfesor().getApellido());
        appbar.setSubtitle("Actividades");
        actividades = new ArrayList<>();
        initAdapter();
        initRecyclerView();

        fabAddActivity = (RevealFAB) findViewById(R.id.fabAddActivity);
        Intent fabIntent = new Intent(PlanDetailActivity.this, AddActivity.class);
        fabIntent.putExtra("PLAN", this.planificacion);
        fabAddActivity.setIntent(fabIntent);
        fabAddActivity.setOnClickListener(new RevealFAB.OnClickListener() {
            @Override
            public void onClick(RevealFAB button, View v) {
                button.startActivityWithAnimation();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        fabAddActivity.onResume();
        getActividades();
    }

    private void initRecyclerView() {
        recycleActividades.setLayoutManager(new LinearLayoutManager(PlanDetailActivity.this));
        recycleActividades.setAdapter(adapter);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent);

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                adapter.clear();
                getActividades();
            }
        });
    }

    private void initAdapter() {
        if (adapter == null) {
            adapter = new ActivityRecycleAdapter(getApplicationContext(), actividades, this);
        }
    }

    @Override
    public void onActivityItemClick(Actividad actividad) {
        Toast.makeText(getApplicationContext(), actividad.getTareas(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, ActividadDetails.class);
        intent.putExtra("ACTIVIDAD", actividad);
        startActivity(intent);
    }

    public void getActividades() {
        swipeRefreshLayout.setRefreshing(true);

        JsonArrayRequest peticion = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response.length() > 0) {
                    ArrayList<Actividad> temp = new ArrayList<>();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject json = response.getJSONObject(i);
                            try {
                                java.sql.Date sqldate = java.sql.Date.valueOf(json.getString("fechaInicial").substring(0,9));
                                Actividad actividad = new Actividad(
                                        json.getInt("idActividad"),
                                        json.getString("contenido"),
                                        new Date(sqldate.getTime()),
                                        new Date(sqldate.getTime()),
                                        json.getString("materiales"),
                                        json.getString("tareas"),
                                        json.getInt("ponderacion"),
                                        json.getString("logro"),
                                        null
                                );
                                temp.add(0, actividad);
                            } catch (Exception e) {
                                Log.e(TAG, "Date parsing error: " + e.getMessage());
                            }


                        } catch (JSONException ex) {
                            Log.e(TAG, "Json parsing error: " + ex.getMessage());
                        }//end try catch
                    }// end for
                    adapter.clear();
                    adapter.update(temp);
                    actividades = temp;
                } else {
                    Toast.makeText(getApplicationContext(), "No existen registros de actividades para esta planificacion", Toast.LENGTH_LONG).show();
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
        getActividades();
    }
}
