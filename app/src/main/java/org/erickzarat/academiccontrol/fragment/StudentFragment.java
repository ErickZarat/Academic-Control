package org.erickzarat.academiccontrol.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.sergiocasero.revealfab.RevealFAB;

import org.erickzarat.academiccontrol.R;
import org.erickzarat.academiccontrol.activity.UserRegistrationActivity;
import org.erickzarat.academiccontrol.adapter.SwipeListAdapter;
import org.erickzarat.academiccontrol.app.Aplication;
import org.erickzarat.academiccontrol.model.Rol;
import org.erickzarat.academiccontrol.model.Usuario;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StudentFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private String TAG = StudentFragment.class.getSimpleName();
    private String URL = "http://academiccontrol-64005.onmodulus.net/api/alumno";

    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView listViewPersonas;
    private SwipeListAdapter swipeListAdapter;
    private List<Usuario> personas;
    private RevealFAB mBtnAdd;

    public StudentFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View ViewRoot = inflater.inflate(R.layout.fragment_student, container, false);
        Toast.makeText(getContext(), "Student Fragment", Toast.LENGTH_SHORT).show();

        listViewPersonas = (ListView) ViewRoot.findViewById(R.id.listViewPersonas);

        personas = new ArrayList<>();
        swipeListAdapter = new SwipeListAdapter(this.getActivity(), personas);
        listViewPersonas.setDivider(this.getResources().getDrawable(R.drawable.transperent_color));
        swipeRefreshLayout = (SwipeRefreshLayout) ViewRoot.findViewById(R.id.swipe_refresh_layout);
        listViewPersonas.setAdapter(swipeListAdapter);

        swipeRefreshLayout.setOnRefreshListener(this);

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent);

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                getPersonas();
            }
        });

        mBtnAdd = (RevealFAB) ViewRoot.findViewById(R.id.fabNewStudent);
        Intent i = new Intent(this.getActivity(), UserRegistrationActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("ROL", new Rol().getAlumno());
        mBtnAdd.setIntent(i);
        ;

        mBtnAdd.setOnClickListener(new RevealFAB.OnClickListener() {
            @Override
            public void onClick(RevealFAB button, View v) {
                button.startActivityWithAnimation();
            }
        });

        return ViewRoot;
    }

    @Override
    public void onResume() {
        super.onResume();
        mBtnAdd.onResume();
    }

    @Override
    public void onRefresh() {
        personas.clear();
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
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject personaJson = response.getJSONObject(i);

                            int idPersona = personaJson.getInt("idUsuario");
                            String name = personaJson.getString("nombre");
                            String email = personaJson.getString("apellido");

                            Usuario persona = new Usuario();
                            persona.setIdUsuario(idPersona);
                            persona.setNombre(name);
                            persona.setApellido(email);
                            personas.add(0, persona);

                        } catch (JSONException ex) {
                            Log.e(TAG, "Json parsing error: " + ex.getMessage());
                        }//end try catch
                    }// end for
                    swipeListAdapter.notifyDataSetChanged();
                }//end if
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
}
