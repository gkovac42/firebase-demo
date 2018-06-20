package com.example.goran.firebasedemo.ui.map;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.goran.firebasedemo.R;
import com.example.goran.firebasedemo.data.model.CheckIn;
import com.example.goran.firebasedemo.ui.viewmodel.CheckInViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Date;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private FusedLocationProviderClient locationProvider;
    private CheckInViewModel viewModel;
    private GoogleMap map;
    private MapView mapView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_map, container, false);

        if (getActivity() != null) {
            MapsInitializer.initialize(getActivity());
            mapView = root.findViewById(R.id.map_view);
            mapView.onCreate(savedInstanceState);
        }

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapView.getMapAsync(this);

        if (getActivity() != null) {
            locationProvider = LocationServices.getFusedLocationProviderClient(getActivity());
        }

        viewModel = ViewModelProviders.of(this).get(CheckInViewModel.class);
        viewModel.getCurrentCheckIn().observe(this, checkIn -> {
            if (checkIn != null) {
                viewModel.saveCheckIn(checkIn);
                goToLocation(checkIn);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        getCurrentLocation();
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        locationProvider.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                CheckIn checkIn = new CheckIn();
                checkIn.setLat(location.getLatitude());
                checkIn.setLng(location.getLongitude());
                checkIn.setDate(new Date());

                viewModel.getCheckInAddress(checkIn);

            } else {
                displayErrorDialog();
            }
        });
    }

    private void goToLocation(CheckIn checkIn) {
        LatLng currentLatLng = new LatLng(checkIn.getLat(), checkIn.getLng());

        MarkerOptions markerOptions = new MarkerOptions()
                .position(currentLatLng)
                .title(checkIn.getAddress());

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentLatLng, 16);

        map.addMarker(markerOptions);
        map.moveCamera(cameraUpdate);
    }

    private void displayErrorDialog() {
        new AlertDialog.Builder(getActivity())
                .setTitle("Error")
                .setMessage("Oops, something went wrong")
                .setPositiveButton("Try again", (dialogInterface, i) -> getCurrentLocation())
                .setNegativeButton("Back", null)
                .show();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
