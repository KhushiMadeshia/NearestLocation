package com.example.nearestlocation.data;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationRepository {

    private final PlacesApiService apiService =
            RetrofitClient.getService();

    public void getNearbyRestaurants(
            String latLng,
            String apiKey,
            RepositoryCallback callback
    ) {



        apiService.getNearbyRestaurants(
                latLng,
                1500,
                "restaurant",
                apiKey
        ).enqueue(new Callback<PlacesResponse>() {

            @Override
            public void onResponse(
                    Call<PlacesResponse> call,
                    Response<PlacesResponse> response
            ) {



                if (response.isSuccessful() && response.body() != null) {
                     callback.onSuccess(response.body());
                } else {
                   callback.onError(new Exception("Empty response"));
                }
            }

            @Override
            public void onFailure(
                    Call<PlacesResponse> call,
                    Throwable t
            ) {
                callback.onError(t);
            }
        });
    }


    public interface RepositoryCallback {
        void onSuccess(PlacesResponse response);
        void onError(Throwable t);
    }
}
