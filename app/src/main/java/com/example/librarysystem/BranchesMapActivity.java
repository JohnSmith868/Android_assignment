package com.example.librarysystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class BranchesMapActivity extends AppCompatActivity implements OnMapReadyCallback {
    GoogleMap map;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;

    private static final int REQUEST_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branches_map);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(4*1000);
        locationRequest.setFastestInterval(2*1000);

        fetchLastLocation();
    }

    private void fetchLastLocation() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION);
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest,new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if(locationResult.getLastLocation() != null){
                    currentLocation = locationResult.getLastLocation();
                    Toast.makeText(getApplicationContext(),currentLocation.getLatitude()+""+currentLocation.getLongitude(),Toast.LENGTH_LONG).show();
                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.branches_map);
                    supportMapFragment.getMapAsync(BranchesMapActivity.this);
                }
            }
        },getMainLooper());


    }


    @Override
    protected void onResume() {
        super.onResume();
        fetchLastLocation();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        float zoomLevel = 16.0f;
        LatLng sportground = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions().position(sportground).title("this is sport ground");
        map.animateCamera(CameraUpdateFactory.newLatLng(sportground));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(sportground,zoomLevel));
        map.addMarker(markerOptions);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_LOCATION:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    fetchLastLocation();
                }
                break;

        }
    }
}
