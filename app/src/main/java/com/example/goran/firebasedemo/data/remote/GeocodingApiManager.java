package com.example.goran.firebasedemo.data.remote;

import com.example.goran.firebasedemo.data.model.GeocodingResponse;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GeocodingApiManager {

    private static final String API_KEY = "AIzaSyATHO_6DIs4uKZNZJo50n95QERyy2Np13w";

    private GeocodingApiService apiService;

    public GeocodingApiManager() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://maps.googleapis.com/")
                .build();

        apiService = retrofit.create(GeocodingApiService.class);
    }

    public Call<GeocodingResponse> getAddress(double lat, double lng) {
        String latLng = lat + "," + lng;
        return apiService.getAddress(latLng, API_KEY);
    }
}
