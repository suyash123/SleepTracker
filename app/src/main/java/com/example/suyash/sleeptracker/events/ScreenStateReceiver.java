package com.example.suyash.sleeptracker.events;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

public class ScreenStateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isScreenOn;
        if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            Log.d("ScreenStateReceiver","Screen went ON");
            isScreenOn = true;
        } else {
            Log.d("ScreenStateReceiver","Screen went OFF");
            isScreenOn = false;
        }
        EventBus.getDefault().post(new ScreenStateEvent(isScreenOn));
    }

    public class ScreenStateEvent {
        private boolean isScreenOn;

        public ScreenStateEvent(boolean isScreenOn) {
            this.isScreenOn = isScreenOn;
        }

        public boolean isScreenOn() {
            return isScreenOn;
        }
    }
}
