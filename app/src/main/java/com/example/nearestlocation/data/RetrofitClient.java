package com.example.nearestlocation.data;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL =
            "https://maps.googleapis.com/maps/api/place/";

    private static Retrofit retrofit;

    public static com.example.nearestlocation.data.PlacesApiService getService() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(com.example.nearestlocation.data.PlacesApiService.class);
    }
}
