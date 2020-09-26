package com.onecricket.location.model;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.onecricket.location.GpsUtils;

import java.util.Locale;

public class LocationServiceManagerImpl implements LocationServiceManager, GpsUtils.onGpsListener {

    private Context context;
    private Listener listener;

    private FusedLocationProviderClient mFusedLocationClient;

    private double wayLatitude = 0.0;
    private double wayLongitude = 0.0;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    private boolean isContinue = false;
    private boolean isGPS = false;


    public LocationServiceManagerImpl(Context context) {
        this.context = context;
    }


    @Override
    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public void startLocationService() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10 * 1000); // 10 seconds
        locationRequest.setFastestInterval(5 * 1000); // 5 seconds

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        wayLatitude = location.getLatitude();
                        wayLongitude = location.getLongitude();
                        if (!isContinue) {
                            listener.onLocationSuccess(wayLatitude,wayLongitude);
                        }
                        if (!isContinue && mFusedLocationClient != null) {
                            mFusedLocationClient.removeLocationUpdates(locationCallback);
                        }
                    }
                }
            }
        };

        GpsUtils gpsUtils = new GpsUtils(context);
        gpsUtils.setListener(this);
        gpsUtils.turnGPSOn();

    }

    @Override
    public void gpsStatus(boolean isGPSEnable) {
        isGPS = isGPSEnable;
        if (isGPSEnable) {
            listener.onGpsEnabled();
        } else {
            listener.onGpsDisabled();
        }
    }

    @Override
    public void askGpsLocationPermission(Exception e) {
        listener.askGpsLocationPermission(e);
    }

    @Override
    public void getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }
        /*if (isContinue) {
            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
        }*/
        mFusedLocationClient.getLastLocation().addOnSuccessListener((Activity) context, location -> {
            if (location != null) {
                wayLatitude = location.getLatitude();
                wayLongitude = location.getLongitude();
                listener.onLocationSuccess(wayLatitude, wayLongitude);
            } else {
                mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
            }
        });
    }


}
