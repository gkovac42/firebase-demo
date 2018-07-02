package com.example.goran.firebasedemo.ui.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.goran.firebasedemo.R;
import com.example.goran.firebasedemo.ui.history.HistoryFragment;
import com.example.goran.firebasedemo.ui.login.LoginActivity;
import com.example.goran.firebasedemo.ui.map.MapFragment;
import com.example.goran.firebasedemo.ui.profile.ProfileFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener {

    private static final int RC_LOCATION_PERMISSIONS = 1;

    private Fragment[] fragments;

    @BindView(R.id.navigation) BottomNavigationView navigation;
    @BindView(R.id.view_pager) ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        navigation.setOnNavigationItemSelectedListener(this);

        addOnPageChangeListener(navigation);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = FirebaseAuth.getInstance()
                .getCurrentUser();

        if (currentUser == null) {
            startActivity(new Intent(this, LoginActivity.class));

        } else {
            if (hasLocationPermissions()) {
                initFragments();
                initPagerAdapter();
                viewPager.setCurrentItem(0);
            } else {
                requestLocationPermissions();
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_my_location:
                viewPager.setCurrentItem(0);
                return true;
            case R.id.navigation_history:
                viewPager.setCurrentItem(1);
                return true;
            case R.id.navigation_profile:
                viewPager.setCurrentItem(2);
                return true;
        }
        return false;
    }

    private boolean hasLocationPermissions() {
        return (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED);
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
                initFragments();
                initPagerAdapter();
                viewPager.setCurrentItem(0);
            } else {
                // permissions denied
                showErrorToast();
            }
        }
    }

    private void initFragments() {
        Fragment mapFragment = new MapFragment();
        Fragment historyFragment = new HistoryFragment();
        Fragment profileFragment = new ProfileFragment();

        fragments = new Fragment[]{mapFragment, historyFragment, profileFragment};
    }

    private void initPagerAdapter() {
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments[position];
            }

            @Override
            public int getCount() {
                return fragments.length;
            }
        });
    }

    private void addOnPageChangeListener(BottomNavigationView navigation) {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                navigation.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void showErrorToast() {
        Toast.makeText(this,
                R.string.msg_permissions_denied,
                Toast.LENGTH_SHORT)
                .show();
    }
}
