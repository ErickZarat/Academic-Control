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
import org.erickzarat.academiccontrol.interfaces.OnTeacherClickListener;
import org.erickzarat.academiccontrol.model.Profesor;

import java.util.List;

/**
 * Created by erick on 3/06/16.
 */
public class TeacherRecycleAdapter extends RecyclerView.Adapter<TeacherRecycleAdapter.ViewHolder> {

    private Context context;
    private List<Profesor> dataset;
    private OnTeacherClickListener onTeacherClickListener;

    public TeacherRecycleAdapter(Context context, List<Profesor> dataset, OnTeacherClickListener onTeacherClickListener) {
        this.context = context;
        this.dataset = dataset;
        this.onTeacherClickListener = onTeacherClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.people_item_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Profesor usr = dataset.get(position);
        holder.setData(usr);
        holder.setOnClickListener(usr, onTeacherClickListener);
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public void add(Profesor usr) {
        dataset.add(usr);
        notifyDataSetChanged();
    }

    public void clear() {
        dataset.clear();
        notifyDataSetChanged();
    }

    public void update(List<Profesor> dataset) {
        this.dataset.clear();
        this.dataset.addAll(dataset);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.txtPonderacion)
        TextView txtId;
        @Bind(R.id.txtName)
        TextView txtName;
        @Bind(R.id.txtProfesor)
        TextView txtNick;
        @Bind(R.id.txtYear)
        TextView txtYear;
        @Bind(R.id.txtSection)
        TextView txtSection;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(Profesor usr) {
            txtId.setText(String.valueOf(usr.getIdUsuario()));
            txtName.setText(usr.getNombre() + " " + usr.getApellido());
            txtNick.setText(usr.getNick());
            txtSection.setText("");
            txtYear.setText("");
        }

        public void setOnClickListener(final Profesor element, final OnTeacherClickListener onTeacherClickListener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onTeacherClickListener.onUserItemClick(element);
                }
            });
        }
    }
}
