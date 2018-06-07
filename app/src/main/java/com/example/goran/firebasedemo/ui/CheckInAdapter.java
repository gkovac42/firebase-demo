package com.example.goran.firebasedemo.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.goran.firebasedemo.R;
import com.example.goran.firebasedemo.data.CheckIn;

import java.util.ArrayList;
import java.util.List;

public class CheckInAdapter extends RecyclerView.Adapter<CheckInAdapter.ViewHolder> {

    private List<CheckIn> checkIns;

    public CheckInAdapter() {
        this.checkIns = new ArrayList<>();
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            TextView txtLocation = holder.itemView.findViewById(R.id.list_item_location);
            txtLocation.setText(checkIns.get(position).toString());

            TextView txtData = holder.itemView.findViewById(R.id.list_item_date);
            txtData.setText(checkIns.get(position).getDate().toString());
    }

    @Override
    public int getItemCount() {
        return checkIns.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View listItem) {
            super(listItem);
        }
    }
}
