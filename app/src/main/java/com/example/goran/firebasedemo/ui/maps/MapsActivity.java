package com.example.goran.firebasedemo.ui.maps;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.example.goran.firebasedemo.R;
import com.example.goran.firebasedemo.ui.CheckInViewModel;
import com.example.goran.firebasedemo.ui.main.MainActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int RC_LOCATION_PERMISSIONS = 1;


    private CheckInViewModel viewModel;
    private GoogleMap map;
    private FusedLocationProviderClient locationProvider;
    private LatLng currentLatLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        initFloatingActionButton();

        viewModel = ViewModelProviders.of(this).get(CheckInViewModel.class);

        locationProvider = LocationServices.getFusedLocationProviderClient(this);
    }

    private void initFloatingActionButton() {
        CoordinatorLayout root = findViewById(R.id.maps_root);
        FloatingActionButton fab = findViewById(R.id.fab_check_in);

        fab.setOnClickListener(view -> viewModel.saveAsBookmark(currentLatLng, task -> {
            if (task.isSuccessful()) {
                Snackbar.make(root, "Bookmark saved!", Snackbar.LENGTH_LONG).show();
            } else {
                Snackbar.make(root, "Error, please try again.", Snackbar.LENGTH_LONG).show();
            }
        }));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        if (hasLocationPermissions()) {
            getCurrentLocation();
        } else {
            requestLocationPermissions();
        }
    }

    private boolean hasLocationPermissions() {
        return (ActivityCompat.checkSelfPermission(
                this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
                this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED);
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        locationProvider.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                        viewModel.saveCheckIn(currentLatLng);
                        goToCurrentLocation();
                    } else {
                        displayErrorDialog();
                    }
                });
    }

    private void goToCurrentLocation() {
        MarkerOptions markerOptions = new MarkerOptions()
                .position(currentLatLng)
                .title("Lat: " + currentLatLng.latitude + ", Lng: " + currentLatLng.longitude);
        map.addMarker(markerOptions);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentLatLng, 15);
        map.moveCamera(cameraUpdate);
    }

    private void requestLocationPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                RC_LOCATION_PERMISSIONS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == RC_LOCATION_PERMISSIONS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "You can't use this feature without granting permissions", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    private void displayErrorDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage("Oops, something went wrong")
                .setPositiveButton("Try again", (dialogInterface, i) -> getCurrentLocation())
                .setNegativeButton("Back", (dialogInterface, i) -> navigateToMainActivity())
                .show();
    }

    private void navigateToMainActivity() {
        startActivity(new Intent(MapsActivity.this, MainActivity.class));
    }
}
