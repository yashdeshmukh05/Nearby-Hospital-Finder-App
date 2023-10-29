package com.example.findnearbyfinder;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import android.Manifest;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.findnearbyfinder.databinding.ActivityMapsBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int Request_code = 101;
    private double lat, lng;
    ImageButton hospital,medical;
   // String google_map_api = "AIzaSyAjZbKQ8PhQeCxYb8RhcH0uk-6CfvwOs5I";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        hospital = findViewById(R.id.hospital);
        medical = findViewById(R.id.medical);

        fusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(this.getApplicationContext());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        hospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // StringBuilder stringBuilder = new StringBuilder
                String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?"+
                "location" + lat +" , "+ lng +
                "&radius = 1000000" +
                "&type=hospital" +
                "&sensor = true" +
                "&key="+getResources().getString(R.string.google_maps_key);


                //String url = stringBuilder.toString();
                Object dataFatch[]=new Object[2];
                dataFatch[0] = mMap;
                // url;
               // Object url;
                dataFatch[1] = url;

                FetchData fetchData = new FetchData();
                fetchData.execute(dataFatch);

            }
        });

        medical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringBuilder stringBuilder = new StringBuilder
                        ("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
                stringBuilder.append("location" +lat+","+lng);
                stringBuilder.append("&radius = 1000000");
                stringBuilder.append("&type=medical");
                stringBuilder.append("&sensor = true");
                stringBuilder.append("&key="+getResources().getString(R.string.google_maps_key));


                String url = stringBuilder.toString();
                Object dataFetch[]=new Object[2];
                dataFetch[0] = mMap;
                // url;
                dataFetch[1] = url;

                FetchData fetchData = new FetchData();
                fetchData.execute(dataFetch);

            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
       getCurrentLocation();
    }

    private void getCurrentLocation()
    {
        if(ActivityCompat.checkSelfPermission(this , Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission
                (this , Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},Request_code);
            return;
        }

        long locationInterval = 60000;
        long locationFastestInterval = 5000;
        long locationMaxWaitTime = 5000;
        LocationRequest locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, locationInterval)
                .setWaitForAccurateLocation(false)
                .setMinUpdateIntervalMillis(locationFastestInterval)
                .setMaxUpdateDelayMillis(locationMaxWaitTime)
                .build();
        LocationCallback locationCallback=new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                Toast.makeText(getApplicationContext(), "Location result is="+locationRequest, Toast.LENGTH_SHORT).show();

                if(locationRequest==null)
                {
                    Toast.makeText(MapsActivity.this, "Current Location is Null", Toast.LENGTH_SHORT).show();
                    return;
                }
               // for (Location location:locationRequest.getLocation()) {
                 //  if (location == null) {
                   //     Toast.makeText(MapsActivity.this, "Current location is" + location.getLongitude(), Toast.LENGTH_SHORT).show();
                    //}
                //}

            }

         /*   @Override
            public void onLocationAvailability(@NonNull LocationAvailability locationAvailability) {
                Toast.makeText(getApplicationContext(), "Location result is="+locationRequest, Toast.LENGTH_SHORT).show();

                if(locationRequest==null)
                {
                    Toast.makeText(MapsActivity.this, "Current Location is Null", Toast.LENGTH_SHORT).show();
                    return;
                }
                //Location location;

               for (Location location:locationRequest.getGranularity(1)){
                  if(location==null){
                        Toast.makeText(MapsActivity.this, "Current location is"+location.getLongitude(), Toast.LENGTH_SHORT).show();
                    }
               }
            }

          */
        };
        fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback,null);
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                if(location != null){
                    lat = location.getLatitude();
                    lng = location.getLongitude();

                    LatLng latLng = new LatLng(lat , lng);
                    mMap.addMarker(new MarkerOptions().position(latLng).title("Current Location"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (Request_code){
            case Request_code:
                if (grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    getCurrentLocation();
                }
        }

    }
}











