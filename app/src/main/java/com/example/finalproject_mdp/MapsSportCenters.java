package com.example.finalproject_mdp;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.finalproject_mdp.databinding.ActivityMapsSportCentersBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class MapsSportCenters extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsSportCentersBinding binding;
    private boolean removeMarker = false;
    FusedLocationProviderClient fusedLocationProviderClient;
    Button getPosition;
    Marker markerCurrent;
    int LOCATION_REQUEST_CODE = 10001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPosition = findViewById(R.id.bt_location);
        binding = ActivityMapsSportCentersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Location
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        //validate permission
   /*     if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getPosition.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (removeMarker == true){
                        markerCurrent.remove();
                        removeMarker = false;
                    }
                    getLocationUser();


                }
            });

        } else {
            askLocationPermission();
        } */
    }
    // to asl permission
 /*   private void askLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            }
        }
    } */
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
        Double latitud = getIntent().getDoubleExtra("lat",0);
        Double longitud = getIntent().getDoubleExtra("long",0);

        // Add a marker in Sydney and move the camera
        LatLng locSportCenter = new LatLng(latitud, longitud);
        mMap.addMarker(new MarkerOptions().position(locSportCenter).title("Marker"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(locSportCenter));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(locSportCenter,15));
    }
  /*  private void getLocationUser() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true); //enable location

        Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(removeMarker == false) {
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    Marker markerCurrent = mMap.addMarker(new MarkerOptions().position(latLng).title("Current Position"));
                    //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,10)); it is only to move camera
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10)); //animate camera
                    removeMarker = true;
                }
            }
        });

    }
*/
}
