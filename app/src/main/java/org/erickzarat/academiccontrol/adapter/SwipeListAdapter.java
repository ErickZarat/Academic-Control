package org.erickzarat.academiccontrol.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.erickzarat.academiccontrol.R;
import org.erickzarat.academiccontrol.model.Usuario;

import java.util.List;

/**
 * Created by erick on 26/05/16.
 */
public class SwipeListAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<Usuario> personas;

    public SwipeListAdapter() {
    }

    public SwipeListAdapter(Activity activity, List<Usuario> personas) {
        this.activity = activity;
        this.personas = personas;
    }

    @Override
    public int getCount() {
        return personas.size();
    }

    @Override
    public Object getItem(int position) {
        return personas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (inflater == null) {
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if (view == null) {
            view = inflater.inflate(R.layout.list_row, null);
        }

        TextView tvIdPersona = (TextView) view.findViewById(R.id.tvIdPersona);
        TextView tvName = (TextView) view.findViewById(R.id.tvName);

        tvIdPersona.setText(String.valueOf(personas.get(position).getIdUsuario()));
        tvName.setText(personas.get(position).getNombre());

        view.findViewById(R.id.card_user).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return view;
    }
}
