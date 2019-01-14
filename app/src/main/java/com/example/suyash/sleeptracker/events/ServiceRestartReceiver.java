package com.example.suyash.sleeptracker.events;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.suyash.sleeptracker.TrackerService;

public class ServiceRestartReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("ServiceRestartReceiver", "Service restart receiver.");
        context.startService(new Intent(context, TrackerService.class));
    }
}
