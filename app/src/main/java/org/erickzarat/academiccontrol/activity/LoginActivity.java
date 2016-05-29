package org.erickzarat.academiccontrol.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.erickzarat.academiccontrol.R;
import org.erickzarat.academiccontrol.volley.WebService;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText txtNick;
    private EditText txtContrasena;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.appbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (Build.VERSION.SDK_INT > 21) {
            changeStatusBarColor();
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        txtNick = (EditText) findViewById(R.id.txt_nick_login);
        txtContrasena = (EditText) findViewById(R.id.txt_contrasena_login);

        findViewById(R.id.btn_signin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authUser(txtNick.getText().toString(), txtContrasena.getText().toString());
            }
        });
    }

    @TargetApi(21)
    public void changeStatusBarColor() {
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
    }


    public void authUser(String nick, String contrasena) {
        Map<String, String> usr = new HashMap<String, String>();
        usr.put("nick", nick);
        usr.put("contrasena", contrasena);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, WebService.autenticar, new JSONObject(usr), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("Data: ", "nombre: " + response.getString("nombre"));
                    Toast.makeText(getApplicationContext(), "nombre: " + response.getString("nombre"), Toast.LENGTH_SHORT);
                } catch (Exception ex) {
                    Log.e("Request Exception", ex.getMessage());
                    Toast.makeText(getApplicationContext(), "Eception", Toast.LENGTH_SHORT);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error: Response ", error.getMessage());
                Toast.makeText(getApplicationContext(), "Error Volley", Toast.LENGTH_SHORT);
            }
        });
        WebService.getInstance(LoginActivity.this).addToRequestQueue(request);
    }
}
