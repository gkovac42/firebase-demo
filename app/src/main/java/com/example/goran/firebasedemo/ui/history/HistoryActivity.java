package com.example.goran.firebasedemo.ui.history;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.goran.firebasedemo.R;
import com.example.goran.firebasedemo.ui.CheckInAdapter;
import com.example.goran.firebasedemo.ui.CheckInViewModel;

public class HistoryActivity extends AppCompatActivity {

    private CheckInViewModel viewModel;
    private RecyclerView recyclerView;
    private CheckInAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        adapter = new CheckInAdapter();

        recyclerView = findViewById(R.id.recycler_history);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        viewModel = ViewModelProviders.of(this).get(CheckInViewModel.class);

        viewModel.getCheckInHistory(e -> {
            // display error
        }).observe(this, checkIns -> {
            adapter.addAll(checkIns);
            adapter.notifyDataSetChanged();
        });
    }
}
