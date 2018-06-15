package com.example.goran.firebasedemo.ui.map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.goran.firebasedemo.R;
import com.example.goran.firebasedemo.ui.CheckInAdapter;
import com.example.goran.firebasedemo.ui.CheckInViewModel;
import com.example.goran.firebasedemo.ui.login.LoginActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int RC_LOCATION_PERMISSIONS = 1;

    private FusedLocationProviderClient locationProvider;
    private CheckInViewModel viewModel;
    private SupportMapFragment mapFragment;
    private GoogleMap map;
    private CheckInAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        locationProvider = LocationServices.getFusedLocationProviderClient(this);

        adapter = new CheckInAdapter();

        initRecyclerView();

        viewModel = ViewModelProviders.of(this).get(CheckInViewModel.class);

        if (hasLocationPermissions()) {
            mapFragment.getMapAsync(this);

            viewModel.getCheckInHistory(e -> displayToast(getString(R.string.error_history)))
                    .observe(this, checkIns -> adapter.addAll(checkIns));
        } else {
            requestLocationPermissions();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_sign_out) {
            signOut();
        }
        return false;
    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this, LoginActivity.class));
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    private boolean hasLocationPermissions() {
        return (ActivityCompat.checkSelfPermission(
                this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
                this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestLocationPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                RC_LOCATION_PERMISSIONS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == RC_LOCATION_PERMISSIONS) {
            // check if request code matches location permissions
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // permissions granted
                mapFragment.getMapAsync(this);
                displayToast(getString(R.string.msg_permissions_granted));
            } else {
                // permissions denied
                displayToast(getString(R.string.msg_permissions_denied));
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        getCurrentLocation();

        adapter.setListener(checkIn -> {
            LatLng latLng = new LatLng(checkIn.getLatitude(), checkIn.getLongitude());
            goToLocation(latLng);
        });
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        locationProvider.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());

                        viewModel.saveCheckIn(currentLatLng);

                        goToLocation(currentLatLng);

                    } else {
                        displayErrorDialog();
                    }
                });
    }

    private void goToLocation(LatLng latLng) {
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .title("Lat: " + latLng.latitude + ", Lng: " + latLng.longitude);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 16);

        map.clear();
        map.addMarker(markerOptions);
        map.moveCamera(cameraUpdate);
    }

    private void displayToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void displayErrorDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage("Oops, something went wrong")
                .setPositiveButton("Try again", (dialogInterface, i) -> getCurrentLocation())
                .setNegativeButton("Back", null)
                .show();
    }


}
