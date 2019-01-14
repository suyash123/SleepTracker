package com.example.suyash.sleeptracker.events;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

public class LocationInfoReceiver implements LocationListener {


    @Override
    public void onLocationChanged(Location location) {
        Log.d("LocationInfoReceiver" , "Location changed : lat -" + location.getLatitude() + " , lon-" + location.getLongitude());
        EventBus.getDefault().post(new LocationEvent("Location Changed."));
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public class LocationEvent {
        private String locChangeMsg;

        public LocationEvent(String locChangeMsg) {
            this.locChangeMsg = locChangeMsg;
        }

        public String getLocChangeMsg() {
            return locChangeMsg;
        }
    }
}
