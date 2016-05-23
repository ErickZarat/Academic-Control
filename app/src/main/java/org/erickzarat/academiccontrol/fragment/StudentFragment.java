package org.erickzarat.academiccontrol.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.erickzarat.academiccontrol.R;

public class StudentFragment extends Fragment {

    public StudentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View ViewRoot = inflater.inflate(R.layout.fragment_student, container, false);
        Toast.makeText(getContext(), "Student Fragment", Toast.LENGTH_SHORT).show();

        return ViewRoot;
    }
}
