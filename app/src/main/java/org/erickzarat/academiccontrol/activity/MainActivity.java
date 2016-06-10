package org.erickzarat.academiccontrol.activity;

/**
 * Created by erick on 21/05/16.
 */

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import org.erickzarat.academiccontrol.R;
import org.erickzarat.academiccontrol.fragment.GradeFragment;
import org.erickzarat.academiccontrol.fragment.PlanificacionFragment;
import org.erickzarat.academiccontrol.fragment.UserFragment;
import org.erickzarat.academiccontrol.helper.DatabaseHelper;
import org.erickzarat.academiccontrol.helper.NotificationHelper;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {


    @Bind(R.id.appbar)
    Toolbar appbar;
    @Bind(R.id.navview)
    NavigationView navview;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(appbar);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_nav_menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*
        //Eventos del Drawer Layout
        drawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        */

        navview.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        boolean fragmentTransaction = false;
                        Fragment fragment = null;

                        switch (menuItem.getItemId()) {
                            case R.id.menu_users:
                                fragment = new UserFragment();
                                fragmentTransaction = true;
                                break;
                            case R.id.menu_grades:
                                fragment = new GradeFragment();
                                fragmentTransaction = true;
                                break;
                            case R.id.menu_login_option:
                                DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext(), "Utilities", null, 1);
                                SQLiteDatabase db = dbHelper.getWritableDatabase();
                                db.execSQL("UPDATE Utilities SET value='false' WHERE name='logged'");
                                db.close();
                                Intent in = new Intent(MainActivity.this, LoginActivity.class);
                                in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(in);
                                break;
                            case R.id.menu_plan:
                                fragment = new PlanificacionFragment();
                                fragmentTransaction = true;
                                break;
                        }

                        if (fragmentTransaction) {
                            transacFragment(fragment, menuItem.getTitle().toString());
                            menuItem.setChecked(true);
                        }

                        drawerLayout.closeDrawers();

                        return true;
                    }
                });
        Intent intent = new Intent(this, NotificationHelper.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 60000 , pendingIntent);
        Toast.makeText(getApplicationContext(),"Alarm setted",Toast.LENGTH_SHORT).show();

    }

    public void transacFragment(Fragment fragment, String title) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();
        getSupportActionBar().setTitle(title);
    }

    public void replaceFragment(Fragment fragment, String title) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_navigation, fragment)
                .commit();

        getSupportActionBar().setTitle(title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
