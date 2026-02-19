package com.example.nearestlocation.ui;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nearestlocation.R;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        double lat = getIntent().getDoubleExtra("LATITUDE", 0.0);
        double lng = getIntent().getDoubleExtra("LONGITUDE", 0.0);

        Log.d("HOME_LOCATION", "Lat: " + lat + ", Lng: " + lng);
    }
}
