package org.erickzarat.academiccontrol.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.erickzarat.academiccontrol.R;
import org.erickzarat.academiccontrol.helper.WebServiceHelper;
import org.erickzarat.academiccontrol.model.Rol;
import org.erickzarat.academiccontrol.model.Usuario;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserRegistrationActivity extends AppCompatActivity {

    private EditText txt_first_name;
    private EditText txt_sec_name;
    private EditText txt_nick;
    private EditText txt_contrasena;
    private Button btnRegister;
    private Switch sw_teacher_rol;
    private Rol actualRol;

    @Override
    protected void onCreate(Bundle savedInstanceState) throws NullPointerException {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);

        Toolbar toolbar = (Toolbar) findViewById(R.id.appbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (Build.VERSION.SDK_INT > 21) {
            changeStatusBarColor();
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                onBackPressed();
            }
        });

        sw_teacher_rol = (Switch) findViewById(R.id.sw_teacher_rol);

        Rol rol = (Rol) getIntent().getSerializableExtra("ROL");

        if (rol.getIdRol() == 1) {//profesor
            sw_teacher_rol.setChecked(true);
        } else {//alumno
            sw_teacher_rol.setChecked(false);
        }

        actualRol = rol;
        txt_first_name = (EditText) findViewById(R.id.txt_first_name);
        txt_sec_name = (EditText) findViewById(R.id.txt_sec_name);
        txt_nick = (EditText) findViewById(R.id.txt_nick);
        txt_contrasena = (EditText) findViewById(R.id.txt_contrasena);

        btnRegister = (Button) findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrar();
            }
        });


    }

    public void registrar() {
        Usuario usr = new Usuario();
        usr.setNombre(txt_first_name.getText().toString());
        usr.setApellido(txt_sec_name.getText().toString());
        usr.setNick(txt_nick.getText().toString());
        usr.setContrasena(txt_contrasena.getText().toString());
        usr.setRol(new Rol(actualRol.getIdRol(), actualRol.getNombreRol()));
        registerUser(usr);
        onBackPressed();
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
            registrar();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @TargetApi(21)
    public void changeStatusBarColor() {
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
    }

    public void registerUser(Usuario usuario) {
        final boolean requestOk = false;
        Map<String, String> usr = new HashMap<>();
        usr.put("idUsuario", null);
        usr.put("nombre", usuario.getNombre());
        usr.put("apellido", usuario.getApellido());
        usr.put("nick", usuario.getNick());
        usr.put("contrasena", usuario.getContrasena());
        usr.put("idRol", "" + usuario.getRol().getIdRol());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                WebServiceHelper.getInstance(UserRegistrationActivity.this).ROUTE_USUARIO, new JSONObject(usr),
                new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Toast.makeText(getApplicationContext(), "Mensaje: " + response.getString("Mensaje"),
                            Toast.LENGTH_SHORT).show();
                } catch (Exception ex) {
                    Log.e("Request Exception", ex.getMessage());
                    Toast.makeText(getApplicationContext(), "Eception", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error: Response ", error.getMessage());
                Toast.makeText(getApplicationContext(), "Error Volley", Toast.LENGTH_SHORT).show();
            }
        });
        WebServiceHelper.getInstance(UserRegistrationActivity.this).addToRequestQueue(request);
    }
}
