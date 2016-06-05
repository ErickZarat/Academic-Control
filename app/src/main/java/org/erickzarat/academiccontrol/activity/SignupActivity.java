package org.erickzarat.academiccontrol.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import org.erickzarat.academiccontrol.R;
import org.erickzarat.academiccontrol.model.Rol;
import org.erickzarat.academiccontrol.model.Usuario;

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
    }

    public void signup() {
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
                txtContrasena.getText().toString(),
                new Rol(0,"Undefined"));

        // TODO: Implement your own signup logic here.

        new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    public void onSignupSuccess() {
        btnSignup.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        btnSignup.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;


        String first = txtFirstName.getText().toString();
        String sec = txtSecName.getText().toString();
        String nick = txtNick.getText().toString();
        String pass =txtContrasena.getText().toString();

        if (first.isEmpty() || first.length() < 3) {
            txtFirstName.setError("at least 3 characters");
            valid = false;
        } else {
            txtFirstName.setError(null);
        }

        if (sec.isEmpty() || sec.length() < 3) {
            txtSecName.setError("at least 3 characters");
            valid = false;
        } else {
            txtSecName.setError(null);
        }

        if (nick.isEmpty() || nick.length() < 3) {
            txtNick.setError("at least 3 characters");
            valid = false;
        } else {
            txtNick.setError(null);
        }

        if (pass.isEmpty() || pass.length() < 4 || pass.length() > 10) {
            txtContrasena.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            txtContrasena.setError(null);
        }

        return valid;
    }
}
