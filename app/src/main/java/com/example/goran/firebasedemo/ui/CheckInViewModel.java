package com.example.goran.firebasedemo.ui;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.example.goran.firebasedemo.data.CheckIn;
import com.example.goran.firebasedemo.data.FirestoreManager;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

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

    public void saveAsBookmark(LatLng latLng, OnCompleteListener<DocumentReference> onCompleteListener) {
        firestoreManager.saveAsBookmark(latLng)
                .addOnCompleteListener(onCompleteListener);
    }

    public LiveData<List<CheckIn>> getCheckInHistory(OnFailureListener onFailureListener) {
        MutableLiveData<List<CheckIn>> checkIns = new MutableLiveData<>();

        firestoreManager.getAllCheckIns()
                .addOnFailureListener(onFailureListener)
                .addOnSuccessListener(snapshots -> checkIns.setValue(snapshots.toObjects(CheckIn.class)));

        return checkIns;
    }

    public LiveData<CheckIn> getLastCheckIn(OnFailureListener onFailureListener) {
        MutableLiveData<CheckIn> lastCheckIn = new MutableLiveData<>();

        firestoreManager.getAllCheckIns()
                .addOnFailureListener(onFailureListener)
                .addOnSuccessListener(snapshots -> {
                    if (snapshots.size() > 0) {
                        DocumentSnapshot snapshot = snapshots.getDocuments().get(snapshots.size() - 1);
                        CheckIn checkIn = snapshot.toObject(CheckIn.class);
                        lastCheckIn.setValue(checkIn);
                    }
                });

        return lastCheckIn;
    }

    public LiveData<List<CheckIn>> getBookmarks(OnFailureListener onFailureListener) {
        MutableLiveData<List<CheckIn>> bookmarks = new MutableLiveData<>();

        firestoreManager.getBookmarks()
                .addOnFailureListener(onFailureListener)
                .addOnSuccessListener(snapshots -> bookmarks.setValue(snapshots.toObjects(CheckIn.class)));

        return bookmarks;
    }
}