package com.example.nearestlocation.ui;

import android.location.Location;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

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

        userLat = getIntent().getDoubleExtra("LATITUDE", 0.0);
        userLng = getIntent().getDoubleExtra("LONGITUDE", 0.0);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.mapFragment);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        viewModel = new ViewModelProvider(this)
                .get(LocationViewModel.class);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;

        LatLng userLocation = new LatLng(userLat, userLng);

        googleMap.moveCamera(
                CameraUpdateFactory.newLatLngZoom(userLocation, 15f)
        );

        googleMap.addMarker(
                new MarkerOptions()
                        .position(userLocation)
                        .title("You are here")
        );

        loadHardcodedPlaces();
    }

    private void loadHardcodedPlaces() {

        viewModel.setUserLocation(userLat, userLng);

        viewModel.getNearbyPlaces().observe(this, places -> {

            for (NearbyPlace place : places) {

                LatLng latLng =
                        new LatLng(place.getLatitude(), place.getLongitude());

                Marker marker = googleMap.addMarker(
                        new MarkerOptions()
                                .position(latLng)
                                .title(place.getName())
                                .icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
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

        float km = result[0] / 1000f;

        new AlertDialog.Builder(this)
                .setTitle(place.getName())
                .setMessage(
                        "Distance from your location:\n\n" +
                                String.format("%.2f km", km)
                )
                .setPositiveButton("OK", null)
                .show();
    }
}
