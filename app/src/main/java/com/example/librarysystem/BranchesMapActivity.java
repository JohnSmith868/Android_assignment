package com.example.librarysystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.awareness.snapshot.LocationResponse;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONObject;
import org.w3c.dom.ls.LSOutput;

public class BranchesMapActivity extends AppCompatActivity implements OnMapReadyCallback {
    GoogleMap map;
    Location currentLocation;
    Marker currentlocationMarker;
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
        locationRequest.setInterval(20*1000);
        locationRequest.setFastestInterval(20*1000);

        fetchLastLocation();


    }

    private void fetchLastLocation() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION);
            return;
        }

        LocationSettingsRequest locationSettingsRequest = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest).build();
        SettingsClient client = LocationServices.getSettingsClient(this);

        Task<LocationSettingsResponse> locationSettingsRequestTask = client.checkLocationSettings(locationSettingsRequest);


        locationSettingsRequestTask.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                fusedLocationProviderClient.requestLocationUpdates(locationRequest,new LocationCallback(){
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        if(locationResult.getLastLocation() != null ){
                            currentLocation = locationResult.getLastLocation();
                            Toast.makeText(getApplicationContext(),currentLocation.getLatitude()+""+currentLocation.getLongitude(),Toast.LENGTH_LONG).show();
                            SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.branches_map);
                            if(supportMapFragment!=null){
                                supportMapFragment.getMapAsync(BranchesMapActivity.this);
                            }



                        }
                    }

                }, Looper.myLooper());

            }
        });
        locationSettingsRequestTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if(e instanceof ResolvableApiException){
                    ResolvableApiException apiException = (ResolvableApiException)e;
                    try {
                        apiException.startResolutionForResult(BranchesMapActivity.this, 1234);
                    } catch (IntentSender.SendIntentException ex) {
                        ex.printStackTrace();
                    }
                }

            }
        });







    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.d("branchensmap leavbe","leving");
        fusedLocationProviderClient.removeLocationUpdates(new LocationCallback());

    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchLastLocation();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;
        if(currentlocationMarker!=null){
            currentlocationMarker.remove();
        }

        float zoomLevel = 16.0f;
        LatLng sportground = new LatLng(22.309045,114.178121);

        LatLng currentLocationLatLng = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions().position(currentLocationLatLng).title("this is sport ground");

        map.animateCamera(CameraUpdateFactory.newLatLng(currentLocationLatLng));
//        map.animateCamera(CameraUpdateFactory.newLatLngZoom(sportground,zoomLevel));
         currentlocationMarker = map.addMarker(markerOptions);

//
//         String url = getUrl(currentLocationLatLng,sportground,"driv");
//         RequestQueue queue = Volley.newRequestQueue(this);
//        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.POST, url, null,
//                new Response.Listener<JSONObject>()
//                {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        // display response
//                        Log.d("Response", response.toString());
//                    }
//                },
//                new Response.ErrorListener()
//                {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.d("Error.Response", error.toString());
//                    }
//                }
//        );
//
//        queue.add(getRequest);



    }

    private String getUrl(LatLng from, LatLng to, String directionMode) {
        String str_from = "origin="+from.latitude+","+from.longitude;
        String str_to = "destination="+to.latitude+","+to.longitude;
        String mode = "mode="+directionMode;
        String parameters = str_from+"&"+str_to+"&"+mode;
        String outputformat = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/"+outputformat+"?"+parameters+"&key="+getString(R.string.map_api_key);
        return url;

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
