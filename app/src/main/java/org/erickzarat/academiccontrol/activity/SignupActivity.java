package org.erickzarat.academiccontrol.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import org.erickzarat.academiccontrol.R;
import org.erickzarat.academiccontrol.helper.EncryptHelper;
import org.erickzarat.academiccontrol.helper.KeyboardHelper;
import org.erickzarat.academiccontrol.helper.WebServiceHelper;
import org.erickzarat.academiccontrol.model.Rol;
import org.erickzarat.academiccontrol.model.Usuario;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";
    @Bind(R.id.txt_first_name)
    EditText txtFirstName;
    @Bind(R.id.txt_sec_name)
    EditText txtSecName;
    @Bind(R.id.txt_nick)
    EditText txtNick;
    @Bind(R.id.txt_contrasena)
    EditText txtContrasena;
    @Bind(R.id.btn_signup)
    AppCompatButton btnSignup;
    @Bind(R.id.link_login)
    TextView linkLogin;
    private boolean requestOk = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        linkLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public void goBack() {
        startActivity(new Intent(SignupActivity.this, LoginActivity.class));
        this.finish();
    }

    public void signup() {
        KeyboardHelper.getInstance().hideKeyboard(SignupActivity.this);
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        btnSignup.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        Usuario usr = new Usuario(0,
                txtFirstName.getText().toString(),
                txtSecName.getText().toString(),
                txtNick.getText().toString(),
                EncryptHelper.getInstance().MD5Encrypt(txtContrasena.getText().toString()),
                new Rol(2, "Undefined"));

        registerUser(usr);

        new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        if (requestOk) {
                            onSignupSuccess();
                        } else {
                            onSignupFailed();
                        }

                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    public void onSignupSuccess() {
        btnSignup.setEnabled(true);
        setResult(RESULT_OK, null);
        Toast.makeText(getBaseContext(), "Sign up Success", Toast.LENGTH_LONG).show();
        goBack();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Sign up failed", Toast.LENGTH_LONG).show();

        btnSignup.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;


        String first = txtFirstName.getText().toString();
        String sec = txtSecName.getText().toString();
        String nick = txtNick.getText().toString();
        String pass = txtContrasena.getText().toString();

        if (first.isEmpty() || first.length() < 3) {
            txtFirstName.setError(getString(R.string.character_error));
            valid = false;
        } else {
            txtFirstName.setError(null);
        }

        if (sec.isEmpty() || sec.length() < 3) {
            txtSecName.setError(getString(R.string.character_error));
            valid = false;
        } else {
            txtSecName.setError(null);
        }

        if (nick.isEmpty() || nick.length() < 3) {
            txtNick.setError(getString(R.string.character_error));
            valid = false;
        } else {
            txtNick.setError(null);
        }

        if (pass.isEmpty() || pass.length() < 4 || pass.length() > 10) {
            txtContrasena.setError(getString(R.string.password_error));
            valid = false;
        } else {
            txtContrasena.setError(null);
        }

        return valid;
    }

    public void registerUser(Usuario usuario) {
        Map<String, String> usr = new HashMap<>();
        usr.put("idUsuario", null);
        usr.put("nombre", usuario.getNombre());
        usr.put("apellido", usuario.getApellido());
        usr.put("nick", usuario.getNick());
        usr.put("contrasena", usuario.getContrasena());
        usr.put("idRol", "" + usuario.getRol().getIdRol());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                WebServiceHelper.getInstance(SignupActivity.this).ROUTE_USUARIO, new JSONObject(usr),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Toast.makeText(getApplicationContext(), "Mensaje: " + response.getString("Mensaje"),
                                    Toast.LENGTH_SHORT).show();
                            requestOk = true;
                        } catch (Exception ex) {
                            Log.e("Request Exception", ex.getMessage());
                            Toast.makeText(getApplicationContext(), "Exception", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error: Response ", error.getMessage());
                Toast.makeText(getApplicationContext(), "Error Volley", Toast.LENGTH_SHORT).show();
            }
        });
        WebServiceHelper.getInstance(SignupActivity.this).addToRequestQueue(request);
    }
}
