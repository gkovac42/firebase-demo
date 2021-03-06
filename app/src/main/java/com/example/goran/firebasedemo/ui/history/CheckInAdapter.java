package com.example.goran.firebasedemo.ui.history;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.goran.firebasedemo.R;
import com.example.goran.firebasedemo.data.model.CheckIn;

import java.util.ArrayList;
import java.util.List;

public class CheckInAdapter extends RecyclerView.Adapter<CheckInAdapter.ViewHolder> {

    private List<CheckIn> checkIns;
    private AdapterOnClickListener listener;

    public CheckInAdapter() {
        this.checkIns = new ArrayList<>();
    }

    public interface AdapterOnClickListener {

        void onClick(CheckIn checkIn);
    }

    public void setListener(AdapterOnClickListener listener) {
        this.listener = listener;
    }

    public void add(CheckIn checkIn) {
        this.checkIns.add(checkIn);
        notifyDataSetChanged();
    }

    public void addAll(List<CheckIn> checkIns) {
        this.checkIns.addAll(checkIns);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TextView txtLocation = holder.itemView.findViewById(R.id.list_item_location);
        txtLocation.setText(checkIns.get(position).getAddress());

        TextView txtData = holder.itemView.findViewById(R.id.list_item_date);
        txtData.setText(checkIns.get(position).getDate().toString());

        holder.itemView.setOnClickListener(view -> {
            if (listener != null) {
                listener.onClick(checkIns.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return checkIns.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ViewHolder(View listItem) {
            super(listItem);
        }
    }
}
