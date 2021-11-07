package com.example.finalproject_mdp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private ArrayList<String> history;

    public HistoryAdapter(ArrayList<String> dataSet) {
        history = dataSet;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create View
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_row, parent, false);
        return new ViewHolder(view);
    }

    public void add(String data) {
        history.add(data);
        this.notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mTextView.setText(history.get(position));
    }

    @Override
    public int getItemCount() {
        return history.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView mTextView;
        public ViewHolder(View view) {
            super(view);
            mTextView = view.findViewById(R.id.row_text);
        }
    }
}
