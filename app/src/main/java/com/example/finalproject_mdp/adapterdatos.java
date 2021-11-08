package com.example.finalproject_mdp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class adapterdatos extends RecyclerView.Adapter<adapterdatos.ViewHolderDatos> {

    //recibe lista para mostrar en el recycler
    private ArrayList<String> listDatos;
    private ArrayList<String> listSchedules;
    private Context context;
    private static final String TAG = "NotesRecyclerAdapter";
   // private OnRecyclerListener onRecyclerListener;
   private OnRecyclerListener monRecyclerListener;

    //constructor
    public adapterdatos(Context context, ArrayList<String> listDatos,ArrayList<String> listSchedules, OnRecyclerListener onRecyclerListener) {
        this.context = context;
        this.listDatos = listDatos;
        this.listSchedules = listSchedules;
        this.monRecyclerListener = onRecyclerListener;

    }

    //enlazar adaptador con itemlist
    @Override
    public ViewHolderDatos onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items,parent,false);
        return new ViewHolderDatos(view, monRecyclerListener);
    }

    //establecer la comunicacion entre el adaptador y la clase ViewHolderDatos
    @Override
    public void onBindViewHolder(ViewHolderDatos holder, int position) {
        //set the data in items
        holder.tvDatos.setText(listDatos.get(position));
        holder.tvschedules.setText(listSchedules.get(position));
    }

    @Override
    public int getItemCount() {
        return listDatos.size();
    }

    //show data and click implementation
    public class ViewHolderDatos extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvDatos;
        TextView tvschedules;
        OnRecyclerListener onRecyclerListener;
        public ViewHolderDatos(View itemView, OnRecyclerListener onRecyclerListener) {
            super(itemView);
            tvDatos = itemView.findViewById(R.id.tv_id_items);
            tvschedules = itemView.findViewById(R.id.tv_id_items2);
            this.onRecyclerListener = onRecyclerListener;
            itemView.setOnClickListener(this);

        }
        public void onClick(View view) {
            Log.d(TAG, "onClick: " + getAdapterPosition());
            onRecyclerListener.onRecyclerClick(getAdapterPosition());
        }
    }
    // Click Listener interface for Recycle View
    public interface OnRecyclerListener{
        void onRecyclerClick(int position);
    }
}
