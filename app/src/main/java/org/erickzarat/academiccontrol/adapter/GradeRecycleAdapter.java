package org.erickzarat.academiccontrol.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import org.erickzarat.academiccontrol.R;
import org.erickzarat.academiccontrol.model.Nota;

import java.util.List;

/**
 * Created by erick on 3/06/16.
 */
public class GradeRecycleAdapter extends RecyclerView.Adapter<GradeRecycleAdapter.ViewHolder> {
    public Context context;
    public List<Nota> dataset;

    public GradeRecycleAdapter(Context context, List<Nota> dataset) {
        this.context = context;
        this.dataset = dataset;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grade_item_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setData(dataset.get(position));
    }

    @Override
    public int getItemCount() {
        return this.dataset.size();
    }

    public void clear() {
        dataset.clear();
        notifyDataSetChanged();
    }

    public void add(Nota nota) {
        dataset.add(0, nota);
        notifyDataSetChanged();
    }

    public void update(List<Nota> grades) {
        dataset.clear();
        dataset.addAll(grades);
        notifyDataSetChanged();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.txtPonderacion)
        TextView txtPonderacion;
        @Bind(R.id.txtTarea)
        TextView txtTarea;
        @Bind(R.id.txtNick)
        TextView txtNick;
        @Bind(R.id.txtNota)
        TextView txtNota;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(Nota nota) {
            txtNick.setText(nota.getAlumno().getNick());
            txtNota.setText("Obtenido:"+String.valueOf(nota.getPunteo()));
            txtPonderacion.setText("Total:"+String.valueOf(nota.getActividad().getPonderacion()));
            txtTarea.setText(nota.getActividad().getTareas());
        }
    }
}
