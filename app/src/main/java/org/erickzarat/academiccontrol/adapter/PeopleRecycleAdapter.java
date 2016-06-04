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
import org.erickzarat.academiccontrol.model.Usuario;

import java.util.List;

/**
 * Created by erick on 3/06/16.
 */
public class PeopleRecycleAdapter extends RecyclerView.Adapter<PeopleRecycleAdapter.ViewHolder> {

    private Context context;
    private List<Usuario> dataset;

    public PeopleRecycleAdapter(Context context, List<Usuario> dataset) {
        this.context = context;
        this.dataset = dataset;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.people_item_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setData(dataset.get(position));
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public void add(Usuario usr){
        dataset.add(usr);
        notifyDataSetChanged();
    }

    public void clear(){
        dataset.clear();
        notifyDataSetChanged();
    }

    public void update(List<Usuario> dataset){
        this.dataset.clear();
        this.dataset.addAll(dataset);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.tvIdPersona)
        TextView tvIdPersona;
        @Bind(R.id.tvName)
        TextView tvName;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(Usuario usr){
            tvIdPersona.setText(String.valueOf(usr.getIdUsuario()));
            tvName.setText(usr.getNombre() + " " + usr.getApellido() );
        }
    }
}
