package com.example.suyash.sleeptracker;

import android.Manifest;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.evernote.android.job.JobManager;
import com.example.suyash.sleeptracker.entities.job.MyJobCreator;
import com.example.suyash.sleeptracker.events.LightDetector;
import com.example.suyash.sleeptracker.events.LocationInfoReceiver;
import com.example.suyash.sleeptracker.events.MovementDetector;
import com.example.suyash.sleeptracker.events.ScreenStateReceiver;

public class TrackerService extends Service {

    public static final String LOC_PERMISSION_BROADCAST = "loc_perm_broadcast";
    public static final long UPDATE_INTERVAL_TIME_MS = 5000l;
    public static final float DISTANCE_UPDATE_M = 10.0f;
    private JobManager jobManager;
    private ScreenStateReceiver screenStateReceiver;

    public TrackerService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("TrackerService", "Service Started [flags : " + flags + ", startId : " + startId);

        jobManager = JobManager.create(this);
        LocalBroadcastManager.getInstance(this).registerReceiver(permBroadcastReceiver, new IntentFilter(LOC_PERMISSION_BROADCAST));
        EventsTracker.getInstance(this).registerEvents();

        initService();

        return super.onStartCommand(intent, flags, startId);
    }

    private void initService() {
        requestLocationUpdates();
        registerScreenOnOffEvents();
        MovementDetector.getInstance(this).start();
        LightDetector.getInstance(this).start();
    }



    private void requestLocationUpdates() {
        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = service.getBestProvider(criteria, false);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        service.requestLocationUpdates(provider, UPDATE_INTERVAL_TIME_MS, DISTANCE_UPDATE_M, new LocationInfoReceiver());
        Log.d("TrackerService", "onLocation listener registered");
    }

    private void registerScreenOnOffEvents(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        screenStateReceiver = new ScreenStateReceiver();
        registerReceiver(screenStateReceiver, intentFilter);
    }

    private BroadcastReceiver permBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            Log.d("permBroadcastReceiver", "Message: " + message);
            requestLocationUpdates();
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(permBroadcastReceiver);
        unregisterReceiver(screenStateReceiver);
        MovementDetector.getInstance(this).stop();
        LightDetector.getInstance(this).stop();
        EventsTracker.getInstance(this).unregisterEvents();

        jobManager.addJobCreator(new MyJobCreator(this));
        super.onDestroy();
    }
}
