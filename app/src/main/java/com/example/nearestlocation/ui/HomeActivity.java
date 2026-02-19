package com.example.nearestlocation.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.nearestlocation.R;
import com.example.nearestlocation.model.NearbyPlace;
import com.example.nearestlocation.viewmodel.LocationViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class HomeActivity extends AppCompatActivity
        implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private LocationViewModel viewModel;

    private double userLat;
    private double userLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Receive user location from SplashActivity
        userLat = getIntent().getDoubleExtra("LATITUDE", 0.0);
        userLng = getIntent().getDoubleExtra("LONGITUDE", 0.0);

        // Setup map fragment
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.mapFragment);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Init ViewModel
        viewModel = new ViewModelProvider(this)
                .get(LocationViewModel.class);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;

        LatLng userLocation = new LatLng(userLat, userLng);


        // Move camera to user location
        googleMap.moveCamera(
                CameraUpdateFactory.newLatLngZoom(userLocation, 15f)
        );

        // Marker for user
        googleMap.addMarker(
                new MarkerOptions()
                        .position(userLocation)
                        .title("You are here")
        );

        // Enable blue dot (permission safe)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED) {

            googleMap.setMyLocationEnabled(true);
        }

        loadNearbyRestaurants();
    }

    private void loadNearbyRestaurants() {

        viewModel.setUserLocation(
                userLat,
                userLng,
                getString(R.string.google_maps_key)
        );



        viewModel.getNearbyPlaces().observe(this, places -> {

            Log.d("MAP_DEBUG", "Places received: " +
                    (places == null ? "null" : places.size()));

            if (places == null || places.isEmpty()) {
                Log.d("MAP_DEBUG", "No nearby places found");
                return;
            }

            googleMap.clear();

            googleMap.addMarker(
                    new MarkerOptions()
                            .position(new LatLng(userLat, userLng))
                            .title("You are here")
            );

            for (NearbyPlace place : places) {
                Log.d("MAP_DEBUG", "Adding marker: " + place.getName());

                Marker marker = googleMap.addMarker(
                        new MarkerOptions()
                                .position(new LatLng(
                                        place.getLatitude(),
                                        place.getLongitude()
                                ))
                                .title(place.getName())
                );

                marker.setTag(place);
            }
        });

        googleMap.setOnMarkerClickListener(marker -> {

            if (marker.getTag() instanceof NearbyPlace) {
                showDistanceDialog((NearbyPlace) marker.getTag());
                return true;
            }
            return false;
        });
    }

    private void showDistanceDialog(NearbyPlace place) {

        float[] result = new float[1];

        Location.distanceBetween(
                userLat,
                userLng,
                place.getLatitude(),
                place.getLongitude(),
                result
        );

        float distanceKm = result[0] / 1000f;

        new AlertDialog.Builder(this)
                .setTitle(place.getName())
                .setMessage(
                        "Distance from your location:\n\n" +
                                String.format("%.2f km away", distanceKm)
                )
                .setPositiveButton("OK", null)
                .show();
    }
}
