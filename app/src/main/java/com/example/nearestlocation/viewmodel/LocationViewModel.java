package com.example.nearestlocation.viewmodel;

import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.nearestlocation.model.NearbyPlace;

import java.util.ArrayList;
import java.util.List;

public class LocationViewModel extends ViewModel {

    private final MutableLiveData<List<NearbyPlace>> nearbyPlaces =
            new MutableLiveData<>();

    private double userLat;
    private double userLng;

    public void setUserLocation(double lat, double lng) {
        this.userLat = lat;
        this.userLng = lng;

        loadHardcodedPlaces();
    }

    private void loadHardcodedPlaces() {

        List<NearbyPlace> list = new ArrayList<>();


        
        list.add(new NearbyPlace("Place A", userLat + 0.002, userLng + 0.002));
        list.add(new NearbyPlace("Place B", userLat + 0.003, userLng - 0.002));
        list.add(new NearbyPlace("Place C", userLat - 0.002, userLng + 0.003));
        list.add(new NearbyPlace("Place D", userLat - 0.003, userLng - 0.002));
        list.add(new NearbyPlace("Place E", userLat + 0.0015, userLng - 0.003));

        nearbyPlaces.postValue(list);
    }

    public float distanceFromUser(NearbyPlace place) {
        float[] result = new float[1];

        Location.distanceBetween(
                userLat,
                userLng,
                place.getLatitude(),
                place.getLongitude(),
                result
        );

        return result[0];
    }

    public LiveData<List<NearbyPlace>> getNearbyPlaces() {
        return nearbyPlaces;
    }
}
