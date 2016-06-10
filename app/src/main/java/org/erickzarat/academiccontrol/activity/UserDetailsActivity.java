package org.erickzarat.academiccontrol.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import org.erickzarat.academiccontrol.R;
import org.erickzarat.academiccontrol.model.Alumno;

public class UserDetailsActivity extends AppCompatActivity {

    @Bind(R.id.appbar)
    Toolbar appbar;
    @Bind(R.id.txtTarea)
    TextView txtNombre;
    @Bind(R.id.txtProfesor)
    TextView txtNick;
    @Bind(R.id.txtMateriales)
    TextView txtRol;
    @Bind(R.id.txtPonderacion)
    TextView txtGrado;
    @Bind(R.id.recycle_materias_usuario)
    RecyclerView recycleMateriasUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        ButterKnife.bind(UserDetailsActivity.this);

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

        Alumno alumno = (Alumno) getIntent().getSerializableExtra("ALUMNO");

        txtNombre.setText(alumno.getNombre() + " " + alumno.getApellido());
        txtGrado.setText(alumno.getGrado().getNombreGrado());
        txtNick.setText(alumno.getNick());
        txtRol.setText(alumno.getRol().getNombreRol());
    }
}
