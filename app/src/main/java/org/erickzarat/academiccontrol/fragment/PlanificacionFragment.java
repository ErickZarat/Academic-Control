package org.erickzarat.academiccontrol.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import org.erickzarat.academiccontrol.R;
import org.erickzarat.academiccontrol.activity.PlanDetailActivity;
import org.erickzarat.academiccontrol.adapter.PlanRecycleAdapter;
import org.erickzarat.academiccontrol.app.Aplication;
import org.erickzarat.academiccontrol.helper.WebServiceHelper;
import org.erickzarat.academiccontrol.interfaces.OnPlanClickListener;
import org.erickzarat.academiccontrol.model.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PlanificacionFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, OnPlanClickListener{

    @Bind(R.id.recycle_grade)
    RecyclerView recyclerGrade;
    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    private final String TAG = GradeFragment.class.getSimpleName();
    private String URL = null;

    private PlanRecycleAdapter adapter;
    private List<Planificacion> planificacions;

    public PlanificacionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grade, container, false);
        ButterKnife.bind(this, view);
        URL = WebServiceHelper.getInstance(getActivity().getApplicationContext()).ROUTE_PLANIFICACION;
        planificacions = new ArrayList<Planificacion>();
        initAdapter();
        initRecyclerView();
        return view;
    }

    private void initRecyclerView() {
        recyclerGrade.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerGrade.setAdapter(adapter);
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
        if (adapter == null){
            adapter = new PlanRecycleAdapter(getActivity().getApplicationContext(), planificacions,this);
        }
    }

    private void getGrades(){
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
                                    ),new Profesor(
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
                }else {
                    Toast.makeText(getActivity(), "No existen registros de notas", Toast.LENGTH_SHORT).show();
                }
                swipeRefreshLayout.setRefreshing(false);
                //end on response ();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        Aplication.getInstance(this.getContext()).addToRequestQueue(peticion);

    }

    @Override
    public void onRefresh() {
        getGrades();
    }

    @Override
    public void onPlanClickListener(Planificacion planificacion) {
        Toast.makeText(getActivity().getApplicationContext(), planificacion.getCompetencia(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity().getApplicationContext(), PlanDetailActivity.class);

        intent.putExtra("PLAN", planificacion);

        startActivity(intent);
    }
}
