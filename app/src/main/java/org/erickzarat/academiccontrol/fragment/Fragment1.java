package org.erickzarat.academiccontrol.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.erickzarat.academiccontrol.R;

/**
 * Created by erick on 21/05/16.
 */
public class Fragment1 extends Fragment {

    public Fragment1() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View ViewRoot = inflater.inflate(R.layout.fragment_fragment1, container, false);
        Toast.makeText(getContext(), "Fragmento 1", Toast.LENGTH_SHORT).show();
        return ViewRoot;
    }
}
