package org.erickzarat.academiccontrol.activity;

import android.content.ComponentCallbacks;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.sergiocasero.revealfab.RevealFAB;
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

import java.util.*;

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
    @Bind(R.id.buscar)
    EditText buscar;
    private RevealFAB revealFAB;

    private final String TAG = GradeFragment.class.getSimpleName();
    private String URL = null;

    private PlanRecycleAdapter adapter;
    private List<Planificacion> planificacions;
    private List<Planificacion> backup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);
        ButterKnife.bind(this);

        setSupportActionBar(appbar);

        //setting back arrow into toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //setting subtitle
        appbar.setSubtitle("Planificaciones");

        //handle onClick on back arrow pressed
        appbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //setting color to status bar if is android > lollipop
        if (Build.VERSION.SDK_INT > 21) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        //getting teacher data from intent
        Profesor profesor = (Profesor) getIntent().getSerializableExtra("TEACHER");

        // setting intent to fab for animated activity
        revealFAB = (RevealFAB) findViewById(R.id.fabAddPlan);
        Intent i = new Intent(TeacherActivity.this, AddPlan.class);
        //setting teacher id for add plan in new activity
        i.putExtra("ID", profesor.getIdUsuario());
        //setting intent
        revealFAB.setIntent(i);

        revealFAB.setOnClickListener(new RevealFAB.OnClickListener() {
            @Override
            public void onClick(RevealFAB button, View v) {
                button.startActivityWithAnimation();
            }
        });

        //setting teacher data in the view
        txtNombre.setText(profesor.getNombre());
        txtApellido.setText(profesor.getApellido());
        txtNick.setText(profesor.getNick());

        //getting plans URL for the volley request
        URL = WebServiceHelper.getInstance(getApplicationContext()).ROUTE_PLANIFICACION + "/profesor/" + profesor.getIdUsuario();
        //initialize the arraylist avoiding the null exception
        planificacions = new ArrayList<Planificacion>();
        //initialize the recycle view adapter
        initAdapter();
        //initialize the recycler view
        initRecyclerView();

        //edit text listener for search action
        buscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                search(editable.toString());
            }
        });
    }

    public void search(String s ) {
        //re getting the data if the edit text is empty
        if (s == null || s.isEmpty()) {
           getGrades();
        } else {
            //cleaning for show only the searched elements
            planificacions.clear();
            for (Planificacion p : backup) {
                if (p.getCompetencia().contains(s)) {
                    //add the item if is the searched element
                    planificacions.add(0, p);
                } else if (p.getMateria().getNombreMateria().contains(s)){
                    planificacions.add(0,p);
                }
            }
            adapter.update(planificacions);
            adapter.notifyDataSetChanged();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        //reveal on resume for avoid animation lag
        revealFAB.onResume();
        //updating data after comming back of the add activity
        getGrades();
    }


    private void initRecyclerView() {
        //linear layout manager for show one by one in a list
        recyclePlans.setLayoutManager(new LinearLayoutManager(this));
        recyclePlans.setAdapter(adapter);
        //show swipe animation
        swipeRefreshLayout.setOnRefreshListener(this);
        //setting colors for swipe animation
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
            //initialize the adapter at first time
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
                    backup = temp;
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

    //on swipe pull down
    @Override
    public void onRefresh() {
        getGrades();
    }

    @Override
    public void onPlanClickListener(Planificacion planificacion) {
        Toast.makeText(getApplicationContext(), planificacion.getCompetencia(), Toast.LENGTH_SHORT).show();
        //show plan details on other activity
        Intent intent = new Intent(getApplicationContext(), PlanDetailActivity.class);
        intent.putExtra("PLAN", planificacion);
        //launching activity
        startActivity(intent);
    }
}
