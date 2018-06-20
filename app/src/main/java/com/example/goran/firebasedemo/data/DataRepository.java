package com.example.goran.firebasedemo.data;

import com.example.goran.firebasedemo.data.model.CheckIn;
import com.example.goran.firebasedemo.data.model.GeocodingResponse;
import com.example.goran.firebasedemo.data.remote.FirestoreManager;
import com.example.goran.firebasedemo.data.remote.GeocodingApiManager;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

import retrofit2.Call;

public class DataRepository {

    private FirestoreManager firestoreManager;
    private GeocodingApiManager geocodingApiManager;

    public DataRepository(FirestoreManager firestoreManager, GeocodingApiManager geocodingApiManager) {
        this.firestoreManager = firestoreManager;
        this.geocodingApiManager = geocodingApiManager;
    }

    public void saveCheckIn(CheckIn checkIn) {
        firestoreManager.saveCheckIn(checkIn);
    }


    public Task<QuerySnapshot> getAllCheckIns() {
        return firestoreManager.getAllCheckIns();
    }

    public Call<GeocodingResponse> getCheckAddress(CheckIn checkIn) {
        return geocodingApiManager.getAddress(checkIn.getLat(), checkIn.getLng());
    }
}
