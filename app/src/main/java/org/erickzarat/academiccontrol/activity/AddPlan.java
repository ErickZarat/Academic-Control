package org.erickzarat.academiccontrol.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import fr.ganfra.materialspinner.MaterialSpinner;
import org.erickzarat.academiccontrol.R;
import org.erickzarat.academiccontrol.helper.WebServiceHelper;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddPlan extends AppCompatActivity {

    @Bind(R.id.appbar)
    Toolbar appbar;
    @Bind(R.id.spinBim)
    MaterialSpinner spinBim;
    @Bind(R.id.spinGrado)
    MaterialSpinner spinGrado;
    @Bind(R.id.txtCompetencia)
    EditText txtCompetencia;

    private int idBim, idMat, idGra, idUsu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plan);
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
        getGrados();
        getBimestre();
        getMateria();
        idUsu = getIntent().getIntExtra("ID", 0);
    }

    public void getGrados() throws NullPointerException {
        String[] ITEMS = {"4to", "5to", "6to"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ITEMS);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        MaterialSpinner spinner = (MaterialSpinner) findViewById(R.id.spinGrado);
        spinner.setAdapter(adapter1);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (adapterView.getItemAtPosition(i).toString()){
                    case "4to":
                        idGra = 1;
                        break;
                    case "5to":
                        idGra = 2;
                        break;
                    case "6to":
                        idGra = 3;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void getBimestre() throws NullPointerException {
        String[] ITEMS = {"1er Bimestre", "2do Bimestre", "3er Bimestre", "4to Bimestre", "5to Bimestre"};
        final ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ITEMS);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        MaterialSpinner spinner = (MaterialSpinner) findViewById(R.id.spinBim);
        spinner.setAdapter(adapter1);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String s = adapterView.getItemAtPosition(i).toString();
                if (s.contains("1")){
                    idBim=1;
                }else if (s.contains("2")){
                    idBim=2;
                }else if(s.contains("3")){
                    idBim=3;
                }else if (s.contains("4")){
                    idBim=4;
                }else{
                    idBim=5;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
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

    public void getMateria() throws NullPointerException {
        String[] ITEMS = {"Matematica", "Taller", "Lenguaje","Fisica","Teoria"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ITEMS);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        MaterialSpinner spinner = (MaterialSpinner) findViewById(R.id.spinMateria);
        spinner.setAdapter(adapter1);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (adapterView.getItemAtPosition(i).toString()){
                    case "Matematica":
                        idMat=1;
                        break;
                    case "Taller":
                        idMat=2;
                        break;
                    case "Lenguaje":
                        idMat=3;
                        break;
                    case "Fisica":
                        idMat=4;
                        break;
                    case "Teoria":
                        idMat=5;
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void agregar() {
        final boolean requestOk = false;
        Map<String, String> usr = new HashMap<>();
        usr.put("idPlanificacion", null);
        usr.put("competencia", txtCompetencia.getText().toString());
        usr.put("idBimestre", String.valueOf(idBim));
        usr.put("idMateria", String.valueOf(idMat));
        usr.put("idUsuario", String.valueOf(idUsu));
        usr.put("idGrado", String.valueOf(idGra));

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

}
