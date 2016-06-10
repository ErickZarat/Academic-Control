package org.erickzarat.academiccontrol.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ProgressBar;
import butterknife.Bind;
import butterknife.ButterKnife;
import org.erickzarat.academiccontrol.R;
import org.erickzarat.academiccontrol.helper.DatabaseHelper;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreenActivity extends Activity {

    // Set the duration of the splash screen
    private static final long SPLASH_SCREEN_DELAY = 1500;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set portrait orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // Hide title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_splash_screen);
        ButterKnife.bind(this);

        progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#208D4E"), PorterDuff.Mode.MULTIPLY);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                dbHelper = new DatabaseHelper(SplashScreenActivity.this, "Utilities", null, 1);

                boolean logged = getLoggedvalue();
                Log.e("LOGGED =============", String.valueOf(logged));
                Intent intent = null;
                if (!logged) {
                    intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                } else {
                    intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                }

                startActivity(intent);
                finish();
            }
        };

        // Simulate a long loading process on application startup.
        Timer timer = new Timer();
        timer.schedule(task, SPLASH_SCREEN_DELAY);
    }

    public boolean getLoggedvalue() {

        boolean logged = false;
        String[] campos = new String[]{"name", "value"};
        String[] args = new String[]{"logged"};

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.query("Utilities", campos, "name=?", args, null, null, null);

//Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {
            do {
                String name = c.getString(0);
                Boolean val = Boolean.parseBoolean(c.getString(1));
                logged = val;
            } while (!c.isFirst());
        } else {
            dbHelper.insert(1, "logged", "false");
        }
        db.close();

        return logged;
    }

}