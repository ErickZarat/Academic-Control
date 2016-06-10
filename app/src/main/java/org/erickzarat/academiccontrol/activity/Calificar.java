package org.erickzarat.academiccontrol.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import org.erickzarat.academiccontrol.R;
import org.erickzarat.academiccontrol.adapter.StudentRecycleAdapter;
import org.erickzarat.academiccontrol.app.Aplication;
import org.erickzarat.academiccontrol.helper.WebServiceHelper;
import org.erickzarat.academiccontrol.interfaces.OnStudentClickListener;
import org.erickzarat.academiccontrol.model.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Calificar extends AppCompatActivity implements OnStudentClickListener {

    @Bind(R.id.appbar)
    Toolbar appbar;
    @Bind(R.id.txtTarea)
    EditText txtTarea;
    @Bind(R.id.txtNota)
    EditText txtNota;
    @Bind(R.id.recycle_Alumnos)
    RecyclerView recycleAlumnos;

    private int idDetalle, idActiv;


    private final String TAG = ActividadDetails.class.getSimpleName();
    private String URL = null;

    private StudentRecycleAdapter adapter;
    private List<Alumno> alumnos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calificar);
        ButterKnife.bind(this);
        URL = WebServiceHelper.getInstance(getApplicationContext()).ROUTE_ALUMNO;
        setSupportActionBar(appbar);
        alumnos = new ArrayList<>();
        if (Build.VERSION.SDK_INT > 21) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
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
        idActiv = actividad.getIdActividad();
        initAdapter();
        initRecyclerView();
    }


    private void initRecyclerView() {
        recycleAlumnos.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recycleAlumnos.setAdapter(adapter);
        getPersonas();
    }

    private void initAdapter() {
        if (adapter == null){
            adapter = new StudentRecycleAdapter(getApplicationContext(), alumnos, this);
        }
    }

    public void getPersonas() {

        JsonArrayRequest peticion = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                ArrayList<Alumno> temp = new ArrayList<Alumno>();
                if (response.length() > 0) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject personaJson = response.getJSONObject(i);

                            Alumno persona = new Alumno();
                            persona.setIdUsuario(personaJson.getInt("idDetalleAlumno"));
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
                    alumnos.clear();
                    adapter.update(temp);
                    alumnos = temp;
                }else {
                    Toast.makeText(getApplicationContext(), "No existen registros de alumnos",Toast.LENGTH_SHORT).show();
                }

                //end on response ();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();


            }
        });

        Aplication.getInstance(getApplicationContext()).addToRequestQueue(peticion);
    }

    @Override
    public void onStudentItemClick(Alumno alumno) {
        idDetalle = alumno.getIdUsuario();
        Toast.makeText(getApplicationContext(), "Se ha seleccionado el alumno: "+alumno.getNick(), Toast.LENGTH_SHORT).show();
    }

    public void agregar() {
        final boolean requestOk = false;
        Map<String, Integer> usr = new HashMap<>();
        usr.put("idNota", null);
        usr.put("idDetalleAlumno", idDetalle);
        usr.put("idActividad", idActiv);
        usr.put("punteo", Integer.parseInt(txtNota.getText().toString()));

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                WebServiceHelper.getInstance(this).ROUTE_NOTA, new JSONObject(usr),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Toast.makeText(getApplicationContext(), "Mensaje: " + response.getString("mensaje"),
                                    Toast.LENGTH_SHORT).show();
                        } catch (Exception ex) {
                            Log.e("Request Exception", ex.getMessage());
                            Toast.makeText(getApplicationContext(), "Exception", Toast.LENGTH_SHORT).show();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save) {
            agregar();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
