package org.erickzarat.academiccontrol.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.sergiocasero.revealfab.RevealFAB;
import org.erickzarat.academiccontrol.R;
import org.erickzarat.academiccontrol.activity.UserDetailsActivity;
import org.erickzarat.academiccontrol.activity.UserRegistrationActivity;
import org.erickzarat.academiccontrol.adapter.StudentRecycleAdapter;
import org.erickzarat.academiccontrol.app.Aplication;
import org.erickzarat.academiccontrol.helper.WebServiceHelper;
import org.erickzarat.academiccontrol.interfaces.OnStudentClickListener;
import org.erickzarat.academiccontrol.model.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StudentFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, OnStudentClickListener {

    @Bind(R.id.recycle_students)
    RecyclerView recycleStudents;
    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.fabNewStudent)
    RevealFAB fabNewStudent;

    private String TAG = StudentFragment.class.getSimpleName();
    private String URL = null;
    private StudentRecycleAdapter adapter;
    private List<Alumno> personas;

    public StudentFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_student, container, false);
        ButterKnife.bind(this, view);
        URL = WebServiceHelper.getInstance(getActivity().getApplicationContext()).ROUTE_ALUMNO;
        personas = new ArrayList<>();
        initAdapter();
        initRecyclerView();


        fabNewStudent = (RevealFAB) view.findViewById(R.id.fabNewStudent);
        Intent i = new Intent(this.getActivity(), UserRegistrationActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("ROL", new Rol().getAlumno());
        fabNewStudent.setIntent(i);

        fabNewStudent.setOnClickListener(new RevealFAB.OnClickListener() {
            @Override
            public void onClick(RevealFAB button, View v) {
                button.startActivityWithAnimation();
            }
        });

        return view;
    }

    private void initRecyclerView() {
        recycleStudents.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycleStudents.setAdapter(adapter);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent);

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                adapter.clear();
                getPersonas();
            }
        });
    }

    private void initAdapter() {
        if (adapter == null){
            adapter = new StudentRecycleAdapter(getActivity().getApplicationContext(), personas, this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        fabNewStudent.onResume();
    }

    @Override
    public void onRefresh() {
        getPersonas();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void getPersonas() {
        swipeRefreshLayout.setRefreshing(true);

        JsonArrayRequest peticion = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                ArrayList<Alumno> temp = new ArrayList<Alumno>();
                if (response.length() > 0) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject personaJson = response.getJSONObject(i);

                            Alumno persona = new Alumno();
                            persona.setIdUsuario(personaJson.getInt("idUsuario"));
                            persona.setNombre(personaJson.getString("nombre"));
                            persona.setApellido(personaJson.getString("apellido"));
                            persona.setNick(personaJson.getString("nick"));

                            persona.setRol(new Rol(
                                    personaJson.getInt("idRol"),personaJson.getString("nombreRol")));
                            Alumno a = (Alumno) persona;
                            a.setGrado(new Grado(
                                    personaJson.getInt("idGrado"), personaJson.getString("nombreGrado")));
                            a.setSeccion(new Seccion(
                                    personaJson.getInt("idSeccion"), personaJson.getString("nombreSeccion")));

                            temp.add(0, persona);

                        } catch (JSONException ex) {
                            Log.e(TAG, "Json parsing error: " + ex.getMessage());
                        }//end try catch
                    }// end for
                    personas.clear();
                    adapter.update(temp);
                    personas = temp;
                }else {
                    Toast.makeText(getActivity(), "No existen registros de alumnos",Toast.LENGTH_SHORT).show();
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
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onStudentItemClick(Alumno usuario) {
        Toast.makeText(getActivity().getApplicationContext(), usuario.getNombre(),Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity().getApplicationContext(), UserDetailsActivity.class);
        intent.putExtra("ALUMNO", usuario);
        startActivity(intent);
    }
}
