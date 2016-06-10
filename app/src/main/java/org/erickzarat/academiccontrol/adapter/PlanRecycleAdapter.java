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
import org.erickzarat.academiccontrol.interfaces.OnPlanClickListener;
import org.erickzarat.academiccontrol.model.Planificacion;

import java.util.List;

/**
 * Created by erick on 6/06/16.
 */
public class PlanRecycleAdapter extends RecyclerView.Adapter<PlanRecycleAdapter.ViewHolder> {

    private Context context;
    private List<Planificacion> dataset;
    private OnPlanClickListener onPlanClickListener;

    public PlanRecycleAdapter(Context context, List<Planificacion> dataset, OnPlanClickListener onPlanClickListener) {
        this.context = context;
        this.dataset = dataset;
        this.onPlanClickListener = onPlanClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.plan_item_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Planificacion plan = dataset.get(position);
        holder.setData(plan);
        holder.setOnClickListener(plan, onPlanClickListener);
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public void add(Planificacion planificacion) {
        dataset.add(planificacion);
        notifyDataSetChanged();
    }

    public void clear() {
        dataset.clear();
        notifyDataSetChanged();
    }

    public void update(List<Planificacion> dataset) {
        this.dataset.clear();
        this.dataset.addAll(dataset);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.txtPonderacion)
        TextView txtGrado;
        @Bind(R.id.txtTarea)
        TextView txtCompetencia;
        @Bind(R.id.txtMateriales)
        TextView txtMateria;
        @Bind(R.id.txtLogro)
        TextView txtBimestre;
        @Bind(R.id.txtProfesorName)
        TextView txtProfesorName;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(Planificacion planificacion) {
            txtGrado.setText(planificacion.getGrado().getNombreGrado());
            txtBimestre.setText(planificacion.getBimestre().getNombreBimestre());
            txtCompetencia.setText(planificacion.getCompetencia());
            txtMateria.setText(planificacion.getMateria().getNombreMateria());
            txtProfesorName.setText(planificacion.getProfesor().getNombre() + " " + planificacion.getProfesor().getApellido());
        }

        public void setOnClickListener(final Planificacion element, final OnPlanClickListener onPlanClickListener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onPlanClickListener.onPlanClickListener(element);
                }
            });
        }
    }
}
