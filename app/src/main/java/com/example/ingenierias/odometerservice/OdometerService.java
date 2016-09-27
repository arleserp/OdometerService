package com.example.ingenierias.odometerservice;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

/**
 * Created by Ingenierias on 26/09/2016.
 */
public class OdometerService extends Service {
    private static double distanceInMeters;
    private static Location lastLocation = null;
    public final IBinder binder = new OdometerBinder();

    @Override
    public void onCreate() {
        LocationListener listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (lastLocation == null) {
                    lastLocation = location;
                }
                distanceInMeters += location.distanceTo(lastLocation);
                lastLocation = location;
            }

            @Override
            public void onProviderDisabled(String arg0) {
            }

            @Override
            public void onProviderEnabled(String arg0) {
            }

            @Override
            public void onStatusChanged(String arg0, int arg1, Bundle bundle) {
            }
        };

        LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        String locationProvider = locManager.getBestProvider(criteria, true);

        locManager.requestLocationUpdates(locationProvider, 1000, 1, listener);
    }

    public double getMiles() {
        return this.distanceInMeters / 1609.344;
    }

    public class OdometerBinder extends Binder {
        OdometerService getOdometer(){
            return OdometerService.this;

        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
}
