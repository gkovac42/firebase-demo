package com.example.goran.firebasedemo.data.remote;

import com.example.goran.firebasedemo.data.model.GeocodingResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GeocodingApiService {

    @GET("maps/api/geocode/json")
    Call<GeocodingResponse> getAddress(@Query("latlng") String latLng,
                                       @Query("key") String apiKey);
}
