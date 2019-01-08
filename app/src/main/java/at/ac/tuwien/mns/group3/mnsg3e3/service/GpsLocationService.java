package at.ac.tuwien.mns.group3.mnsg3e3.service;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import at.ac.tuwien.mns.group3.mnsg3e3.model.Location;
import at.ac.tuwien.mns.group3.mnsg3e3.util.SimpleFuture;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class GpsLocationService {


    @SuppressLint("MissingPermission")
    public Location getGpsLocationSync(Context ctx) {

        SimpleFuture<Location> future = new SimpleFuture<>();
        LocationManager locationManager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
        SimpleLocationListener listener = new SimpleLocationListener(future);
        locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, listener, ctx.getMainLooper());

        try {
            Location l = future.get(60, TimeUnit.SECONDS);
            //Location l = future.get();
            Log.i(getClass().getName(), "Received a GPS Location: " + l.toString());
            return l;
        } catch (ExecutionException | TimeoutException | InterruptedException e) {
            Log.w(getClass().getName(), "Timeout in getGpsLocation");
            Location l = new Location(48.1905858,16.3320705, 0.1d);
            Log.i(getClass().getName(), "Using Test GPS Location: " + l.toString());
            return l;
        }
    }


    private class SimpleLocationListener implements LocationListener {

        private boolean complete;
        private SimpleFuture<Location> future;

        public SimpleLocationListener(SimpleFuture<Location> future) {
            this.future = future;
        }

        @Override
        public void onLocationChanged(final android.location.Location location) {
            Log.i(getClass().getName(), "Actually received a location: " + location);
            if (complete) {
                return;
            }
            Location l = new Location(location.getLatitude(), location.getLongitude(), location.getAccuracy());
            complete = true;
            future.put(l);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }
    }
}
