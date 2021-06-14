package com.alican.graduationproject1;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class PostLocationActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;
    String postLatitude,postLongitude,postTitle,postExplanation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_location);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        Intent valuesIntent = getIntent();
        postLatitude = valuesIntent.getStringExtra("latitude");
        postLongitude = valuesIntent.getStringExtra("longitude");
        postTitle = valuesIntent.getStringExtra("title");
        postExplanation = valuesIntent.getStringExtra("explanation");


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                System.out.println(" Location : " + location.toString());
                /*
                LatLng userLocation = new LatLng(location.getLatitude(),location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,15));
                */
            }
        };


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            // Permission istemek yok ise
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else{
            // Location alma
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,1,locationListener);
            // Last Location
            Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastLocation != null){
                LatLng userLastLocation = new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude());
                LatLng postLocation = new LatLng(Double.parseDouble(postLatitude),Double.parseDouble(postLongitude));

                mMap.addMarker(new MarkerOptions().position(userLastLocation).title("Konumum").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)) ).showInfoWindow();
                mMap.addMarker(new MarkerOptions().position(postLocation).title(postTitle).snippet(postExplanation).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))).showInfoWindow();
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(postLocation,15));

            }

        }





        // Add a marker in Sydney and move the camera
      /*  LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
    }
}