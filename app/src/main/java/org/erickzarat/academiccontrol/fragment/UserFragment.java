package org.erickzarat.academiccontrol.fragment;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.sergiocasero.revealfab.RevealFAB;

import org.erickzarat.academiccontrol.R;
import org.erickzarat.academiccontrol.activity.MainActivity;


public class UserFragment extends Fragment {


    public UserFragment() {
        // Required empty public constructor
    }

    private Fragment fragment;
    private boolean fragmentTransaction;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_user, container, false);

        AHBottomNavigation bottomNavigation = (AHBottomNavigation) rootView.findViewById(R.id.bottom_navigation);

        AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.teacher_nav, R.mipmap.ic_student_menu, R.color.nav_color1);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.student_nav, R.mipmap.ic_student_menu, R.color.nav_color2);

        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);

        bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#FFFFFF"));

        bottomNavigation.setBehaviorTranslationEnabled(false);

        bottomNavigation.setAccentColor(Color.parseColor("#FFFFFF"));
        bottomNavigation.setInactiveColor(Color.parseColor("#747474"));

        bottomNavigation.setForceTint(false);

        bottomNavigation.setForceTitlesDisplay(true);

        bottomNavigation.setColored(true);

        bottomNavigation.setCurrentItem(0);

        bottomNavigation.setNotificationBackgroundColor(Color.parseColor("#FFFFFF"));

        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position, boolean wasSelected) {
                String title = "";
                RevealFAB fab;
                if (position == 0){
                    fragment = new TeacherFragment();
                    title = getString(R.string.teacher_nav);
                    fab = (RevealFAB) rootView.findViewById(R.id.fabNewTeacher);
                    fragmentTransaction = true;
                }else {
                    fragment = new StudentFragment();
                    title = getString(R.string.student_nav);
                    fragmentTransaction = true;
                    fab = (RevealFAB) rootView.findViewById(R.id.fabNewStudent);
                }
                if(fragmentTransaction) {
                    ((MainActivity)getActivity()).replaceFragment(fragment, title);
                }
            }
        });
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}