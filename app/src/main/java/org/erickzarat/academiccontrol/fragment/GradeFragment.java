package org.erickzarat.academiccontrol.fragment;

import android.content.Context;
import android.net.Uri;
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

import android.widget.Button;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import org.erickzarat.academiccontrol.R;
import org.erickzarat.academiccontrol.adapter.GradeRecycleAdapter;
import org.erickzarat.academiccontrol.app.Aplication;
import org.erickzarat.academiccontrol.model.Grado;
import org.erickzarat.academiccontrol.model.Nota;
import org.erickzarat.academiccontrol.model.Usuario;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GradeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    @Bind(R.id.recycle_grade)
    RecyclerView recyclerGrade;
    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    private final String TAG = GradeFragment.class.getSimpleName();
    private final String URL = "http://192.168.64.13:3000/api/nota";

    private GradeRecycleAdapter adapter;
    private List<Nota> notas;

    public GradeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grade, container, false);
        ButterKnife.bind(this, view);
        notas = new ArrayList<Nota>();
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
                adapter.update(notas);
            }
        });
    }

    private void initAdapter() {
        if (adapter == null){
            adapter = new GradeRecycleAdapter(getActivity().getApplicationContext(), notas);
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

                            Nota newNota = new Nota();
                            newNota.setIdNota(idNota);
                            newNota.setPunteo(punteo);
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
                    Toast.makeText(getActivity(), "No existen registros de notas", Toast.LENGTH_SHORT);
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
}
