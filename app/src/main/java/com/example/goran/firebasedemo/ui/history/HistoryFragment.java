package com.example.goran.firebasedemo.ui.history;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.goran.firebasedemo.R;
import com.example.goran.firebasedemo.ui.viewmodel.CheckInViewModel;

public class HistoryFragment extends Fragment {

    private RecyclerView recyclerView;
    private CheckInAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new CheckInAdapter();

        recyclerView = view.findViewById(R.id.recycler_history);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        CheckInViewModel viewModel = ViewModelProviders.of(this).get(CheckInViewModel.class);
        viewModel.getCheckInHistory(exception -> showErrorToast())
                .observe(this, checkIns -> adapter.addAll(checkIns));
    }

    private void showErrorToast() {
        Toast.makeText(getActivity(),
                R.string.error_generic,
                Toast.LENGTH_SHORT)
                .show();
    }
}
