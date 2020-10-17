package com.onecricket.location.model;

public interface LocationServiceManager {
    void setListener(Listener listener);
    void startLocationService();
    void getLastKnownLocation();
    void turnGpsOn();

    interface Listener {
        void onGpsEnabled();
        void onGpsDisabled();
        void askGpsLocationPermission(Exception e);
        void onLocationSuccess(double wayLatitude, double wayLongitude);
    }
}
