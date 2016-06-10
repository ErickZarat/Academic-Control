package org.erickzarat.academiccontrol.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import org.erickzarat.academiccontrol.R;
import org.erickzarat.academiccontrol.helper.WebServiceHelper;
import org.erickzarat.academiccontrol.model.Planificacion;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddActivity extends AppCompatActivity {

    @Bind(R.id.appbar)
    Toolbar appbar;
    @Bind(R.id.txtContent)
    EditText txtContent;
    @Bind(R.id.txtMateriales)
    EditText txtMateriales;
    @Bind(R.id.txtTarea)
    EditText txtTarea;
    @Bind(R.id.txtPonderacion)
    EditText txtPonderacion;
    @Bind(R.id.txtLogro)
    EditText txtLogro;
    @Bind(R.id.datePicker)
    DatePicker datePicker;

    private Planificacion planificacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_actividad);
        ButterKnife.bind(this);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setSupportActionBar(appbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        planificacion = (Planificacion) getIntent().getSerializableExtra("PLAN");

        appbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save) {
            addActividad();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addActividad() {
        Map<String, String> usr = new HashMap<>();

        try {
            Date fecha = new SimpleDateFormat("dd/MM/yyyy").parse(datePicker.getDayOfMonth() + "/" + (datePicker.getMonth()+1) + "/" + datePicker.getYear());
            java.sql.Date dateSql = new java.sql.Date(fecha.getTime());
            usr.put("fechaInicial", dateSql.toString());
            usr.put("fechaFinal", dateSql.toString());

        } catch (ParseException e) {
            e.printStackTrace();
        }
        usr.put("idActividad", null);
        usr.put("contenido", txtContent.getText().toString());
        usr.put("materiales", txtMateriales.getText().toString());
        usr.put("tareas", txtTarea.getText().toString());
        usr.put("ponderacion", txtPonderacion.getText().toString());
        usr.put("logro", txtLogro.getText().toString());
        usr.put("idPlanificacion", String.valueOf(this.planificacion.getIdPlanificacion()));

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                WebServiceHelper.getInstance(this).ROUTE_ACTIVIDAD, new JSONObject(usr),
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
        WebServiceHelper.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }

}
