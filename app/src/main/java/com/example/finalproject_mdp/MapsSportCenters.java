package com.example.finalproject_mdp;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.finalproject_mdp.databinding.ActivityMapsSportCentersBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MapsSportCenters extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsSportCentersBinding binding;
    private boolean removeMarker = false;
    FusedLocationProviderClient fusedLocationProviderClient;
    Button getPosition;
    Marker markerCurrent;
    int LOCATION_REQUEST_CODE = 10001;
    LocationManager locationManager;
    private static final String TAG = "MAPS ICON";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapsSportCentersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Location
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        //button
        getPosition = findViewById(R.id.bt_location);

        //validate permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

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
        }

    }
    public void getCurrLoc() {

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
        mMap.setMyLocationEnabled(true);
        Location locationGPS = new Location("");
        locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (locationGPS != null) {
            double lat = locationGPS.getLatitude();
            double longi = locationGPS.getLongitude();
            gotoLocation(lat,longi);
        }
     /*   fusedLocationProviderClient.getLastLocation().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                Location location = task.getResult();
                gotoLocation(location.getLatitude(),location.getLongitude());
            }
        }); */

    }

    private void gotoLocation(double latitude, double longitude) {
        LatLng latLng = new LatLng(latitude,longitude);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng,18);
        mMap.moveCamera(cameraUpdate);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
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
    // to ask permission
    private void askLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            }
        }
    }
    //Location
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Permission granted
                getPosition.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getLocationUser();
                    }
                });
            } else {
                //Permission not granted
            }
        }
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

        Double latitud = getIntent().getDoubleExtra("lat",0);
        Double longitud = getIntent().getDoubleExtra("long",0);
        String soccer = getIntent().getStringExtra("soccer");
        String tennis = getIntent().getStringExtra("tennis");
       // Log.d(TAG,tennis);
        String soccer_tennis = getIntent().getStringExtra("soccer_tennis");
        // Add a marker in Sydney and move the camera
        LatLng locSportCenter = new LatLng(latitud, longitud);
        mMap.addMarker(new MarkerOptions().position(locSportCenter).title("Sport Center").icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_baseline_sports_soccer_24)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(locSportCenter));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(locSportCenter,15));
    }
    private BitmapDescriptor bitmapDescriptorFromVector(Context context,int vectorResId){
        Drawable vectorDrawable = ContextCompat.getDrawable(context,vectorResId);

        vectorDrawable.setBounds(0,0,vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),vectorDrawable.getIntrinsicHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
    private void getLocationUser() {

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
                   // markerCurrent.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.tennis));
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    markerCurrent = mMap.addMarker(new MarkerOptions().position(latLng).title("Current Position").icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_baseline_directions_run_24)));
                    //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,10)); it is only to move camera
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15)); //animate camera
                    removeMarker = true;
                }
            }
        });

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
