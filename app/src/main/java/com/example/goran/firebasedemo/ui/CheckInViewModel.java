package com.example.goran.firebasedemo.ui;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.example.goran.firebasedemo.data.CheckIn;
import com.example.goran.firebasedemo.data.FirestoreManager;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;

import java.util.List;

public class CheckInViewModel extends AndroidViewModel {

    private FirestoreManager firestoreManager;

    public CheckInViewModel(@NonNull Application application) {
        super(application);
        firestoreManager = new FirestoreManager();
    }

    public void saveCheckIn(LatLng latLng) {
        firestoreManager.saveCheckIn(latLng);

    }

    public LiveData<List<CheckIn>> getCheckInHistory(OnFailureListener onFailureListener) {
        MutableLiveData<List<CheckIn>> checkIns = new MutableLiveData<>();

        firestoreManager.getAllCheckIns()
                .addOnFailureListener(onFailureListener)
                .addOnSuccessListener(snapshots -> checkIns.setValue(snapshots.toObjects(CheckIn.class)));

        return checkIns;
    }
}