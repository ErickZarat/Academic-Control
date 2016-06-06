package org.erickzarat.academiccontrol.activity;

import android.app.ProgressDialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
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
import org.erickzarat.academiccontrol.helper.DatabaseHelper;
import org.erickzarat.academiccontrol.helper.EncryptHelper;
import org.erickzarat.academiccontrol.helper.KeyboardHelper;
import org.erickzarat.academiccontrol.helper.WebServiceHelper;
import org.erickzarat.academiccontrol.model.Rol;
import org.erickzarat.academiccontrol.model.Usuario;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    private Usuario usr;
    private ProgressDialog progressDialog;

    @Bind(R.id.input_email) EditText _emailText;
    @Bind(R.id.input_password) EditText _passwordText;
    @Bind(R.id.btn_login) Button _loginButton;
    @Bind(R.id.link_signup) TextView _signupLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public void login() {
        KeyboardHelper.getInstance().hideKeyboard(this);
        Log.d(TAG, "Login");

        progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);


        progressDialog.show();

        final String email = _emailText.getText().toString();
        final String password = _passwordText.getText().toString();

        authUser(email, password);

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        if (validateUserData()){
                            onLoginSuccess();
                            // onLoginFailed();
                            progressDialog.dismiss();
                        } else {
                            onLoginFailed();
                        }

                    }
                }, 3000);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        progressDialog.dismiss();
        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext(),"Utilities", null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("UPDATE Utilities SET value='true' WHERE name='logged'");
        db.close();

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void onLoginFailed() {
        progressDialog.dismiss();
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() throws NullPointerException{
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
    //for email: || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        if (email.isEmpty()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

    public void setUserData(Usuario usrTemp, boolean valid){
        if (valid){
            this.usr = usrTemp;
        }else {
            this.usr = null;
        }
    }

    public boolean validateUserData(){
        return (usr != null);
    }

    public void authUser(String nick, String contrasena) {
        Map<String, String> usr = new HashMap<>();
        usr.put("nick", nick);
        usr.put("contrasena", EncryptHelper.getInstance().MD5Encrypt(contrasena));

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                WebServiceHelper.getInstance(getApplicationContext()).ROUTE_AUTENTICAR, new JSONObject(usr),
                new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response != null && response.getBoolean("login")){
                        Log.d("Data: ", "nombre: " + response.getString("nombre"));
                        Toast.makeText(getApplicationContext(), "nombre: " + response.getString("nombre"),
                                Toast.LENGTH_SHORT).show();
                        setUserData(new Usuario(
                                response.getInt("idUsuario"),
                                response.getString("nombre"),
                                response.getString("apellido"),
                                response.getString("nick"),
                                response.getString("contrasena"),
                                new Rol(response.getInt("idRol"), "nombre de rol")
                        ), true);
                    }else {
                        setUserData(null, false);
                    }
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

        WebServiceHelper.getInstance(LoginActivity.this).addToRequestQueue(request);
    }
}