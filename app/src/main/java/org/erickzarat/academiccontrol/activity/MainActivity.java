package org.erickzarat.academiccontrol.activity;

/**
 * Created by erick on 21/05/16.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import org.erickzarat.academiccontrol.R;
import org.erickzarat.academiccontrol.fragment.StudentFragment;
import org.erickzarat.academiccontrol.fragment.TeacherFragment;
import org.erickzarat.academiccontrol.fragment.UserFragment;

public class MainActivity extends AppCompatActivity {

    private Toolbar appbar;
    private DrawerLayout drawerLayout;
    private NavigationView navView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        appbar = (Toolbar)findViewById(R.id.appbar);
        setSupportActionBar(appbar);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_nav_menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);

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

        navView = (NavigationView)findViewById(R.id.navview);
        navView.setNavigationItemSelectedListener(
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
                                //fragment = new Fragment2();
                                fragmentTransaction = true;
                                break;
                            case R.id.menu_student:
                                fragment = new StudentFragment();
                                fragmentTransaction = true;
                                break;
                            case R.id.menu_teacher:
                                fragment = new TeacherFragment();
                                fragmentTransaction = true;
                                break;
                            case R.id.menu_reg_option:
                                Intent i = new Intent(MainActivity.this, UserRegistrationActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                                break;
                            case R.id.menu_login_option:
                                Intent in = new Intent(MainActivity.this, LoginActivity.class);
                                in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(in);
                                break;
                        }

                        if(fragmentTransaction) {
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.content_frame, fragment)
                                    .commit();

                            menuItem.setChecked(true);
                            getSupportActionBar().setTitle(menuItem.getTitle());
                        }

                        drawerLayout.closeDrawers();

                        return true;
                    }
                });
    }

    public void replaceFragment(Fragment fragment, String title){
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

        switch(item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
