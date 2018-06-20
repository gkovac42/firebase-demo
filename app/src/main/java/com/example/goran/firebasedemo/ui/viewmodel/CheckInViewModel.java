package com.example.goran.firebasedemo.ui.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.goran.firebasedemo.data.DataRepository;
import com.example.goran.firebasedemo.data.model.CheckIn;
import com.example.goran.firebasedemo.data.model.GeocodingResponse;
import com.example.goran.firebasedemo.data.remote.FirestoreManager;
import com.example.goran.firebasedemo.data.remote.GeocodingApiManager;
import com.google.android.gms.tasks.OnFailureListener;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckInViewModel extends ViewModel {

    private DataRepository dataRepository;

    private MutableLiveData<CheckIn> currentCheckIn;

    public CheckInViewModel() {
        dataRepository = new DataRepository(new FirestoreManager(), new GeocodingApiManager());

        currentCheckIn = new MutableLiveData<>();
    }

    public MutableLiveData<CheckIn> getCurrentCheckIn() {
        return currentCheckIn;
    }

    public void saveCheckIn(CheckIn checkIn) {
        dataRepository.saveCheckIn(checkIn);
    }

    public LiveData<List<CheckIn>> getCheckInHistory(OnFailureListener onFailureListener) {
        MutableLiveData<List<CheckIn>> checkIns = new MutableLiveData<>();

        dataRepository.getAllCheckIns()
                .addOnFailureListener(onFailureListener)
                .addOnSuccessListener(snapshots ->
                        checkIns.setValue(snapshots.toObjects(CheckIn.class)));

        return checkIns;
    }

    public void getCheckInAddress(CheckIn checkIn) {
        dataRepository.getCheckAddress(checkIn).enqueue(new Callback<GeocodingResponse>() {
            @Override
            public void onResponse(@NonNull Call<GeocodingResponse> call,
                                   @NonNull Response<GeocodingResponse> response) {

                GeocodingResponse.Result result = response.body().getResults().get(0);

                checkIn.setAddress(result.getFormattedAddress());

                currentCheckIn.postValue(checkIn);
            }

            @Override
            public void onFailure(@NonNull Call<GeocodingResponse> call,
                                  @NonNull Throwable t) {

                Log.e("ERROR", "Request failed!");
            }
        });
    }
}