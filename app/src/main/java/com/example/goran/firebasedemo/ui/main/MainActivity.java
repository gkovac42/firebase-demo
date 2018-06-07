package com.example.goran.firebasedemo.ui.main;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.goran.firebasedemo.R;
import com.example.goran.firebasedemo.ui.CheckInViewModel;
import com.example.goran.firebasedemo.ui.bookmarks.BookmarksActivity;
import com.example.goran.firebasedemo.ui.history.HistoryActivity;
import com.example.goran.firebasedemo.ui.login.LoginActivity;
import com.example.goran.firebasedemo.ui.maps.MapsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private CheckInViewModel viewModel;

    @BindView(R.id.txt_last_checkin_location) TextView txtLastCheckInLocation;
    @BindView(R.id.txt_last_checkin_date) TextView txtLastCheckInDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        auth = FirebaseAuth.getInstance();

        viewModel = ViewModelProviders.of(this).get(CheckInViewModel.class);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            updateUI();

        } else {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
        }
    }

    private void updateUI() {
        viewModel.getLastCheckIn(error -> Log.e("Error", error.getMessage()))
                .observe(this, checkIn -> {
                    if (checkIn.getDate() != null) {
                        txtLastCheckInLocation.setText(checkIn.toString());
                        txtLastCheckInDate.setText(checkIn.getLocalizedDate());
                    }
                });
    }

    public void onClickMyLocation(View view) {
        startActivity(new Intent(this, MapsActivity.class));
    }

    public void onClickHistory(View view) {
        startActivity(new Intent(this, HistoryActivity.class));
    }

    public void onClickBookmarks(View view) {
        startActivity(new Intent(this, BookmarksActivity.class));
    }
}
