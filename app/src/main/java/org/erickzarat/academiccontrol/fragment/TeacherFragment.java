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
import org.erickzarat.academiccontrol.activity.TeacherActivity;
import org.erickzarat.academiccontrol.activity.UserRegistrationActivity;
import org.erickzarat.academiccontrol.adapter.TeacherRecycleAdapter;
import org.erickzarat.academiccontrol.app.Aplication;
import org.erickzarat.academiccontrol.helper.WebServiceHelper;
import org.erickzarat.academiccontrol.interfaces.OnTeacherClickListener;
import org.erickzarat.academiccontrol.model.Profesor;
import org.erickzarat.academiccontrol.model.Rol;
import org.erickzarat.academiccontrol.model.Usuario;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TeacherFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, OnTeacherClickListener {

    @Bind(R.id.recycle_teachers)
    RecyclerView recycleTeachers;
    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.fabNewTeacher)
    RevealFAB fabNewTeacher;

    //api request elements
    private String TAG = TeacherFragment.class.getSimpleName();
    private String URL = null;

    private List<Profesor> teachers;
    TeacherRecycleAdapter adapter;

    public TeacherFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_teacher, container, false);
        ButterKnife.bind(this, view);
        URL = WebServiceHelper.getInstance(getActivity().getApplicationContext()).ROUTE_PROFESOR;
        teachers = new ArrayList<Profesor>();
        initAdapter();
        initRecyclerView();

        fabNewTeacher = (RevealFAB) view.findViewById(R.id.fabNewTeacher);
        Intent i = new Intent(this.getActivity(), UserRegistrationActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("ROL", new Rol().getProfesor());
        fabNewTeacher.setIntent(i);

        fabNewTeacher.setOnClickListener(new RevealFAB.OnClickListener() {
            @Override
            public void onClick(RevealFAB button, View v) {
                button.startActivityWithAnimation();
            }
        });


        return view;
    }

    private void initRecyclerView() {
        recycleTeachers.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycleTeachers.setAdapter(adapter);
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
            adapter = new TeacherRecycleAdapter(getActivity().getApplicationContext(), teachers, this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        fabNewTeacher.onResume();
    }

    @Override
    public void onRefresh() {
        teachers.clear();
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
                if (response.length() > 0) {
                    ArrayList<Profesor> temp = new ArrayList<Profesor>();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject personaJson = response.getJSONObject(i);

                            Profesor persona = new Profesor();
                            persona.setIdUsuario(personaJson.getInt("idUsuario"));
                            persona.setNombre(personaJson.getString("nombre"));
                            persona.setApellido(personaJson.getString("apellido"));
                            persona.setNick(personaJson.getString("nick"));
                            temp.add(0, persona);

                        } catch (JSONException ex) {
                            Log.e(TAG, "Json parsing error: " + ex.getMessage());
                        }//end try catch
                    }// end for
                    teachers.clear();
                    adapter.update(temp);
                    teachers = temp;
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "No existen registros de profesores", Toast.LENGTH_LONG).show();
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
    public void onUserItemClick(Profesor profesor) {
        Toast.makeText(getActivity().getApplicationContext(), profesor.getNombre(),Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), TeacherActivity.class);
        intent.putExtra("TEACHER", profesor);
        getActivity().startActivity(intent);
    }
}
