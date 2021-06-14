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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class AllPostsInMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;



    ArrayList<String> postIdFromDB;
    ArrayList<String> titleFromFB;
    ArrayList<String> imagesFromFB;
    ArrayList<String> categoryFromFB;
    ArrayList<String> explanationFromFB;
    ArrayList<String> workDateFromFB;
    ArrayList<String> startTimeFromFB;
    ArrayList<String> finishTimeFromFB;
    ArrayList<String> addressFromFB;
    ArrayList<String> latitudeFromFB;
    ArrayList<String> longitudeFromFB;
    ArrayList<String> paymentFromFB;

    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_posts_in_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        Intent valuesIntent = getIntent();


        postIdFromDB = valuesIntent.getStringArrayListExtra("postIdFromDB");
        imagesFromFB = valuesIntent.getStringArrayListExtra("imagesFromFB");
        titleFromFB = valuesIntent.getStringArrayListExtra("titleFromFB");
        categoryFromFB = valuesIntent.getStringArrayListExtra("categoryFromFB");
        explanationFromFB = valuesIntent.getStringArrayListExtra("explanationFromFB");
        workDateFromFB = valuesIntent.getStringArrayListExtra("workDateFromFB");
        startTimeFromFB = valuesIntent.getStringArrayListExtra("startTimeFromFB");
        finishTimeFromFB = valuesIntent.getStringArrayListExtra("finishTimeFromFB");
        addressFromFB = valuesIntent.getStringArrayListExtra("addressFromFB");
        latitudeFromFB = valuesIntent.getStringArrayListExtra("latitudeFromFB");
        longitudeFromFB = valuesIntent.getStringArrayListExtra("longitudeFromFB");
        paymentFromFB = valuesIntent.getStringArrayListExtra("paymentFromFB");


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

                mMap.addMarker(new MarkerOptions().position(userLastLocation).title("Konumum").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))).showInfoWindow();
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLastLocation,15));
                LatLng postLocation;
                String postLatitude,postLongitude,postTitle,postExplanation;

                for (int i = 0; i < titleFromFB.size(); i = i + 1) {

                    postTitle = titleFromFB.get(i);
                    postLatitude = latitudeFromFB.get(i);
                    postLongitude = longitudeFromFB.get(i);
                    postExplanation = explanationFromFB.get(i);

                   // System.out.println( "I : " +  i + " , " + postTitle + " , " + postLatitude + " , " + postLongitude);

                    postLocation = new LatLng(Double.parseDouble(postLatitude),Double.parseDouble(postLongitude));
                    mMap.addMarker(new MarkerOptions().position(postLocation).title(postTitle).snippet(postExplanation).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)) ).showInfoWindow();

                }


            }

        }


        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {

                String indexString =  marker.getId().substring(1);
                Integer position = Integer.parseInt(indexString);
                position = position -1;

                if (position != -1){
                    System.out.println(position  +  ", indis:"  + marker.getSnippet());
                    String postId = postIdFromDB.get(position);
                    String imageUrl = imagesFromFB.get(position);
                    String title = titleFromFB.get(position);
                    String category = categoryFromFB.get(position);
                    String explanation = explanationFromFB.get(position);
                    String workDate = workDateFromFB.get(position);
                    String startTime = startTimeFromFB.get(position);
                    String finishTime = finishTimeFromFB.get(position);
                    String address = addressFromFB.get(position);
                    String latitude = latitudeFromFB.get(position);
                    String longitude = longitudeFromFB.get(position);
                    String payment = paymentFromFB.get(position);

                    Intent postDetayIntent = new Intent(AllPostsInMapActivity.this, PostDetayActivity.class);
                    postDetayIntent.putExtra("post_id",postId);
                    postDetayIntent.putExtra("image_url",imageUrl);
                    postDetayIntent.putExtra("title",title);
                    postDetayIntent.putExtra("category",category);
                    postDetayIntent.putExtra("explanation",explanation);
                    postDetayIntent.putExtra("work_date",workDate);
                    postDetayIntent.putExtra("start_time",startTime);
                    postDetayIntent.putExtra("finish_time",finishTime);
                    postDetayIntent.putExtra("address",address);
                    postDetayIntent.putExtra("latitude",latitude);
                    postDetayIntent.putExtra("longitude",longitude);
                    postDetayIntent.putExtra("payment",payment);
                    startActivity(postDetayIntent);
                }


            }
        });




    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0){
            if (requestCode == 1){
                if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,1,locationListener);
                    Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (lastLocation != null){
                        LatLng userLastLocation = new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude());


                        mMap.addMarker(new MarkerOptions().position(userLastLocation).title("Konumum").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)) ).showInfoWindow();
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLastLocation,15));
                    }
                }
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


}