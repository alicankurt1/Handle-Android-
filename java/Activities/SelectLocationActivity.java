package com.alican.graduationproject1;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class SelectLocationActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;
    String address;
    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLongClickListener(this);

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
                address = gettingAddress(userLastLocation);

                mMap.addMarker(new MarkerOptions().position(userLastLocation).title("Konumum: " + address)).showInfoWindow();
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLastLocation,15));
                alertDialog(address,userLastLocation);
            }

        }



        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
       // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
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
                        address = gettingAddress(userLastLocation);

                        mMap.addMarker(new MarkerOptions().position(userLastLocation).title("Konumum: " + address)).showInfoWindow();
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLastLocation,15));
                        alertDialog(address,userLastLocation);
                    }
                }
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        mMap.clear();
        address = gettingAddress(latLng);
        mMap.addMarker(new MarkerOptions().position(latLng).title(address)).showInfoWindow();

        alertDialog(address,latLng);

    }


    public String gettingAddress(LatLng latLng){
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        address = "";
        try {
            List<Address> addressList = geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
            if (addressList != null && addressList.size() > 0){
                //getting address

                if (addressList.get(0).getThoroughfare() != null){
                    address += addressList.get(0).getThoroughfare();
                }
                if (addressList.get(0).getSubThoroughfare() != null){
                    address += ", " + addressList.get(0).getSubThoroughfare();
                }
                if (addressList.get(0).getSubAdminArea() != null){
                    address += ", " + addressList.get(0).getSubAdminArea();
                }
                if (addressList.get(0).getAdminArea() != null){
                    address += "/" + addressList.get(0).getAdminArea();
                }
                if (addressList.get(0).getPostalCode() != null){
                    address += ", " + addressList.get(0).getPostalCode();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return address;
    }

    public void alertDialog(String address, LatLng latLng){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setCancelable(false);
        alertDialog.setTitle("Adres :");
        alertDialog.setMessage(address);
        alertDialog.setPositiveButton("Seç", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Intent upload activity
                Intent uploadActivityIntent = new Intent(SelectLocationActivity.this,UploadActivity.class);
                String lttd =Double.toString(latLng.latitude);
                String lngt =Double.toString(latLng.longitude);
                uploadActivityIntent.putExtra("address",address);
                uploadActivityIntent.putExtra("latitude",lttd);
                uploadActivityIntent.putExtra("longitude",lngt);

                startActivity(uploadActivityIntent);
            }
        });
        alertDialog.setNegativeButton("Tekrar Seç", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "Tekrar Seç",Toast.LENGTH_LONG).show();
            }
        });
        alertDialog.show();
    }






}