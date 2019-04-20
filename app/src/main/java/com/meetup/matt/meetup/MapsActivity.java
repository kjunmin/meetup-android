package com.meetup.matt.meetup;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.view.View;

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
import com.meetup.matt.meetup.Handlers.LocalStorageHandler;
import com.meetup.matt.meetup.Handlers.SocketHandler;
import com.meetup.matt.meetup.Helpers.GeocodeHelper;
import com.meetup.matt.meetup.Helpers.LocationHelper;
import com.meetup.matt.meetup.Utils.LocationSettingsUtil;
import com.meetup.matt.meetup.Utils.SessionUtil;
import com.meetup.matt.meetup.config.Config;
import com.meetup.matt.meetup.dto.MeetupSessionDTO;
import com.meetup.matt.meetup.dto.UserDTO;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private MeetupSessionDTO meetupSessionDetails;
    private UserDTO userDetails;
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
        meetupSessionDetails = getIntent().getParcelableExtra("sessiondetails");
        userDetails = LocalStorageHandler.getSessionUser(getApplicationContext(), Config.SESSION_FILE_NAME);
        setContentView(R.layout.activity_maps);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        view = findViewById(R.id.map);

        geocodeHelper = new GeocodeHelper(this);

        //start location service
        startMapCallback();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SocketHandler.destroySocketConnection();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        if(LocationSettingsUtil.checkLocationPermission(this)) {
            mMap.setMyLocationEnabled(true);
            mInstanceClient = new MInstanceClient(mMap, this, view, meetupSessionDetails);

        }
    }


    public void onLocationChanged(LatLng latLng) {
        mInstanceClient.onLocationChanged(latLng);
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

    @Override
    public void finish() {
        super.finish();
        if (SessionUtil.isHost(userDetails, meetupSessionDetails)) {
            SocketHandler.getSocket().emit(SocketHandler.Event.Server.DELETE_SESSION, meetupSessionDetails.getSessionId());
        } else {
            SocketHandler.getSocket().emit(SocketHandler.Event.Server.DELETE_USER_FROM_SESSION, userDetails.getUserId(), meetupSessionDetails.getSessionId());
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
