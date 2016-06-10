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
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.sergiocasero.revealfab.RevealFAB;
import fr.ganfra.materialspinner.MaterialSpinner;
import org.erickzarat.academiccontrol.R;
import org.erickzarat.academiccontrol.adapter.PlanRecycleAdapter;
import org.erickzarat.academiccontrol.app.Aplication;
import org.erickzarat.academiccontrol.fragment.GradeFragment;
import org.erickzarat.academiccontrol.helper.WebServiceHelper;
import org.erickzarat.academiccontrol.interfaces.OnPlanClickListener;
import org.erickzarat.academiccontrol.model.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TeacherActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, OnPlanClickListener {

    @Bind(R.id.appbar)
    Toolbar appbar;
    @Bind(R.id.txtNombre)
    TextView txtNombre;
    @Bind(R.id.txtApellido)
    TextView txtApellido;
    @Bind(R.id.txtNick)
    TextView txtNick;
    @Bind(R.id.recycle_plans)
    RecyclerView recyclePlans;
    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    private RevealFAB revealFAB;

    private final String TAG = GradeFragment.class.getSimpleName();
    private String URL = null;

    private PlanRecycleAdapter adapter;
    private List<Planificacion> planificacions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);
        ButterKnife.bind(this);

        setSupportActionBar(appbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        appbar.setTitle("Detalle de Profesor");
        appbar.setSubtitle("Planificaciones");
        appbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if (Build.VERSION.SDK_INT > 21) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        Profesor profesor = (Profesor) getIntent().getSerializableExtra("TEACHER");
        revealFAB = (RevealFAB) findViewById(R.id.fabAddPlan);
        Intent i = new Intent(TeacherActivity.this, AddPlan.class);
        i.putExtra("ID", profesor.getIdUsuario());
        revealFAB.setIntent(i);

        revealFAB.setOnClickListener(new RevealFAB.OnClickListener() {
            @Override
            public void onClick(RevealFAB button, View v) {
                button.startActivityWithAnimation();
            }
        });



        txtNombre.setText(profesor.getNombre());
        txtApellido.setText(profesor.getApellido());
        txtNick.setText(profesor.getNick());

        URL = WebServiceHelper.getInstance(getApplicationContext()).ROUTE_PLANIFICACION + "/profesor/" + profesor.getIdUsuario();
        planificacions = new ArrayList<Planificacion>();
        initAdapter();
        initRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        revealFAB.onResume();
        getGrades();
    }


    private void initRecyclerView() {
        recyclePlans.setLayoutManager(new LinearLayoutManager(this));
        recyclePlans.setAdapter(adapter);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent);

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                adapter.clear();
                getGrades();
                adapter.update(planificacions);
            }
        });
    }

    private void initAdapter() {
        if (adapter == null) {
            adapter = new PlanRecycleAdapter(getApplicationContext(), planificacions, this);
        }
    }

    private void getGrades() {
        swipeRefreshLayout.setRefreshing(true);
        JsonArrayRequest peticion = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                ArrayList<Planificacion> temp = new ArrayList<Planificacion>();
                if (response.length() > 0) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject planJson = response.getJSONObject(i);

                            Planificacion plan = new Planificacion(
                                    planJson.getInt("idPlanificacion"),
                                    new Bimestre(
                                            planJson.getInt("idBimestre"),
                                            planJson.getString("nombreBimestre")
                                    ), new Profesor(
                                    planJson.getInt("idUsuario"),
                                    planJson.getString("nombre"),
                                    planJson.getString("apellido"),
                                    planJson.getString("nick"),
                                    planJson.getString("contrasena"),
                                    new Rol()),
                                    new Materia(
                                            planJson.getInt("idMateria"),
                                            planJson.getString("nombreMateria")),
                                    new Grado(
                                            planJson.getInt("idGrado"),
                                            planJson.getString("nombreGrado")),
                                    planJson.getString("competencia"));
                            temp.add(0, plan);

                        } catch (JSONException ex) {
                            Log.e(TAG, "Json parsing error: " + ex.getMessage());
                        }//end try catch
                    }// end for
                    planificacions.clear();
                    adapter.update(temp);
                    planificacions = temp;
                } else {
                    Toast.makeText(getApplicationContext(), "No existen registros de planificaciones", Toast.LENGTH_SHORT).show();
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

        Aplication.getInstance(this.getApplicationContext()).addToRequestQueue(peticion);

    }

    @Override
    public void onRefresh() {
        getGrades();
    }

    @Override
    public void onPlanClickListener(Planificacion planificacion) {
        Toast.makeText(getApplicationContext(), planificacion.getCompetencia(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), PlanDetailActivity.class);

        intent.putExtra("PLAN", planificacion);

        startActivity(intent);
    }

}
