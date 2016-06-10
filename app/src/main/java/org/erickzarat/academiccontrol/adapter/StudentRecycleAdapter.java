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
import org.erickzarat.academiccontrol.interfaces.OnStudentClickListener;
import org.erickzarat.academiccontrol.model.Alumno;

import java.util.List;

/**
 * Created by erick on 3/06/16.
 */
public class StudentRecycleAdapter extends RecyclerView.Adapter<StudentRecycleAdapter.ViewHolder> {

    private Context context;
    private List<Alumno> dataset;
    private OnStudentClickListener onUserClickListener;

    public StudentRecycleAdapter(Context context, List<Alumno> dataset, OnStudentClickListener onUserClickListener) {
        this.context = context;
        this.dataset = dataset;
        this.onUserClickListener = onUserClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.people_item_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Alumno usr = dataset.get(position);
        holder.setData(usr);
        holder.setOnClickListener(usr, onUserClickListener);
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public void add(Alumno usr) {
        dataset.add(usr);
        notifyDataSetChanged();
    }

    public void clear() {
        dataset.clear();
        notifyDataSetChanged();
    }

    public void update(List<Alumno> dataset) {
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

        public void setData(Alumno usr) {
            txtId.setText(String.valueOf(usr.getIdUsuario()));
            txtName.setText(usr.getNombre() + " " + usr.getApellido());
            txtNick.setText(usr.getNick());
            txtSection.setText(usr.getSeccion().getNombreSeccion());
            txtYear.setText(usr.getGrado().getNombreGrado());
        }

        public void setOnClickListener(final Alumno element, final OnStudentClickListener onUserClickListener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onUserClickListener.onStudentItemClick(element);
                }
            });
        }
    }
}
