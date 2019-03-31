package com.meetup.matt.meetup;

import android.annotation.SuppressLint;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.meetup.matt.meetup.Client.MInstanceClient;
import com.meetup.matt.meetup.Helpers.GeocodeHelper;
import com.meetup.matt.meetup.Helpers.LocationHelper;
import com.meetup.matt.meetup.Utils.LocationSettingsUtil;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {


    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;
    SupportMapFragment mapFragment;
    GeocodeHelper geocodeHelper;
    View view;
    MInstanceClient mInstanceClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        view = findViewById(R.id.map);
        geocodeHelper = new GeocodeHelper(this);

        (findViewById(R.id.btn_add_user)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //start location service
        startMapCallback();
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        if(LocationSettingsUtil.checkLocationPermission(this)) {
            mMap.setMyLocationEnabled(true);
            mInstanceClient = new MInstanceClient(mMap, this, view);
            mInstanceClient.startService();
        }
    }


    public void onLocationChanged(LatLng latLng) {
        mInstanceClient.startDirections(latLng);
    }


    @SuppressLint("MissingPermission")
    private void startMapCallback() {
        mLocationRequest = LocationHelper.createLocationRequest();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (LocationSettingsUtil.checkLocationPermission(this)) {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, new LocationCallback() {
                        @Override
                        public void onLocationResult(LocationResult locationResult) {
                            LatLng currentLatLng = new LatLng(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude());
                            onLocationChanged(currentLatLng);
                        }
                    },
                    Looper.myLooper());
        }

    }
}
