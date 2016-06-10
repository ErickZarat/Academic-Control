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
import org.erickzarat.academiccontrol.interfaces.OnActivityClickListener;
import org.erickzarat.academiccontrol.model.Actividad;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by erick on 6/06/16.
 */
public class ActivityRecycleAdapter extends RecyclerView.Adapter<ActivityRecycleAdapter.ViewHolder> {

    private Context context;
    private List<Actividad> dataset;
    private OnActivityClickListener onActivityClickListener;

    public ActivityRecycleAdapter(Context context, List<Actividad> dataset, OnActivityClickListener onActivityClickListener) {
        this.context = context;
        this.dataset = dataset;
        if (this.dataset == null){
            dataset = new ArrayList<>();
        }
        this.onActivityClickListener = onActivityClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.actividad_item_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Actividad usr = dataset.get(position);
        holder.setData(usr);
        holder.setOnClickListener(usr, onActivityClickListener);
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public void add(Actividad actividad) {
        dataset.add(actividad);
        notifyDataSetChanged();
    }

    public void clear() {
        dataset.clear();
        notifyDataSetChanged();
    }

    public void update(List<Actividad> dataset) {
        this.dataset.clear();
        this.dataset.addAll(dataset);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.txtPonderacion)
        TextView txtPonderacion;
        @Bind(R.id.txtTarea)
        TextView txtTarea;
        @Bind(R.id.txtMateriales)
        TextView txtMateriales;
        @Bind(R.id.txtLogro)
        TextView txtLogro;
        @Bind(R.id.txtDate)
        TextView txtDate;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(Actividad actividad) {
            txtDate.setText(dateFormat.format(actividad.getFechaInicial()) + " - " + dateFormat.format(actividad.getFechaFinal()));
            txtLogro.setText(actividad.getLogro());
            txtMateriales.setText(actividad.getMateriales());
            txtPonderacion.setText(actividad.getPonderacion()+"Pts");
            txtTarea.setText(actividad.getTareas());
        }

        public void setOnClickListener(final Actividad element, final OnActivityClickListener onActivityClickListener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onActivityClickListener.onActivityItemClick(element);
                }
            });
        }
    }
}
