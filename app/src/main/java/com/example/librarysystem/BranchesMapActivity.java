package com.example.librarysystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.ls.LSOutput;

import java.util.ArrayList;
import java.util.HashMap;

public class BranchesMapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    GoogleMap map;
    boolean initCamera = true;
    Location currentLocation;
    Marker currentlocationMarker;
    Polyline currentPolyline = null;
    LatLng clickedMarker;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    Button btnShowRoute;
    ArrayList<HashMap<String, Double>> arrsteps = new ArrayList<>();

    private static final int REQUEST_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branches_map);

        btnShowRoute = findViewById(R.id.map_btn_show_route);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(20*1000);
        locationRequest.setFastestInterval(20*1000);


        fetchLastLocation();

        btnShowRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clickedMarker!=null){
                    if(currentPolyline!=null) {
                        currentPolyline.remove();
                    }
                    System.out.println("marker latlng"+clickedMarker.toString());
                    getRoute();
                }
            }
        });




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


        this.map = googleMap;
        if(currentlocationMarker!=null){

            currentlocationMarker.remove();
        }
        if(currentPolyline!=null){

            System.out.println("polylin is not null");

            currentPolyline.remove();
            map.clear();
            System.out.println("polyline after remove and null"+ currentPolyline.getPoints().toString());
            currentPolyline = null;

        }

        float zoomLevel = 16.0f;
        LatLng sportground = new LatLng(22.309045,114.178121);
        LatLng bookshop2 = new LatLng(22.3933,114.1912);

        LatLng currentLocationLatLng = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions().position(currentLocationLatLng).title("You're here");
        MarkerOptions markerOptionsTarget = new MarkerOptions().position(sportground).title("our book shop");
        MarkerOptions markerOptionsBookshop2 = new MarkerOptions().position(bookshop2).title("our book shop");

        if(initCamera){
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocationLatLng, zoomLevel));
            initCamera = false;
        }


//        map.animateCamera(CameraUpdateFactory.newLatLngZoom(sportground,zoomLevel));
         currentlocationMarker = this.map.addMarker(markerOptions);
        this.map.addMarker(markerOptionsTarget);
        map.addMarker(markerOptionsBookshop2);

        map.setOnMarkerClickListener(this);





    }

    public void getRoute(){
        LatLng currentLocationLatLng = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
        String url = getUrl(currentLocationLatLng,clickedMarker,"walking");
        System.out.println("url: is "+url);
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            arrsteps.clear();
                            System.out.println("arrsteps: afterclear"+ arrsteps.toString());
                            double startLat, startLng, stopLat, stopLng;
                            JSONObject routes = response.getJSONArray("routes").getJSONObject(0);
                            JSONObject legs = routes.getJSONArray("legs").getJSONObject(0);
                            JSONArray  steps = legs.getJSONArray("steps");

                            for(int i = 0; i<steps.length();i++){
                                JSONObject step = steps.getJSONObject(i);
                                JSONObject startlocation = step.getJSONObject("start_location");
                                startLat = startlocation.getDouble("lat");
                                startLng = startlocation.getDouble("lng");
                                JSONObject endlocation = step.getJSONObject("end_location");
                                stopLat = endlocation.getDouble("lat");
                                stopLng = endlocation.getDouble("lng");

                                HashMap<String, Double> hm = new HashMap<>();
                                hm.put("start_lat",startLat);
                                hm.put("start_lng",startLng);
                                hm.put("stop_lat",stopLat);
                                hm.put("stop_lng",stopLng);
                                arrsteps.add(hm);





                            }
                            System.out.println("arrsteps after add+"+ arrsteps.toString());
                            gambarPolyline(arrsteps,Color.BLACK,5.0f);



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        // display response
                        Log.d("Response", response.toString());
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                }
        );

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(getRequest);



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

    public void gambarPolyline(ArrayList<HashMap<String, Double>> arrsteps, int warna, float lebar){
        ArrayList<LatLng> arrayLatlng = new ArrayList<>();

        for(int y = 0; y<arrsteps.size();y++){
            arrayLatlng.add(new LatLng(arrsteps.get(y).get("start_lat"), arrsteps.get(y).get("start_lng")));
            arrayLatlng.add(new LatLng(arrsteps.get(y).get("stop_lat"), arrsteps.get(y).get("stop_lng")));
        }
        System.out.println("array Latlng "+ arrayLatlng);
        PolylineOptions currentPolylineOptions = new  PolylineOptions().clickable(true).addAll(arrayLatlng).color(Color.BLACK).width(5.0f);
        currentPolyline = map.addPolyline(currentPolylineOptions);

        System.out.println("current poly line"+currentPolyline.getPoints().toString());

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

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        this.clickedMarker = marker.getPosition();
        return false;
    }
}
