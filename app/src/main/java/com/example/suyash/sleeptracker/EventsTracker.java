package com.example.suyash.sleeptracker;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;

import com.example.suyash.sleeptracker.entities.DataManager;
import com.example.suyash.sleeptracker.events.LightDetector;
import com.example.suyash.sleeptracker.events.LocationInfoReceiver;
import com.example.suyash.sleeptracker.events.MovementDetector;
import com.example.suyash.sleeptracker.events.ScreenStateReceiver;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class EventsTracker {

    private static final Object lock = new Object();
    private static final String EVENTS_TRACK_HANDLER_THREAD = "EventsTrackThread";
    private static EventsTracker instance;
    private DataManager dataManager;
    private Handler mHandler = null;
    private HandlerThread mHandlerThread = null;

    private EventsTracker(Context context){
        dataManager = new DataManager(context);
        mHandlerThread = new HandlerThread(EVENTS_TRACK_HANDLER_THREAD);
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper());
    }

    public static EventsTracker getInstance(Context context) {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null){
                    instance = new EventsTracker(context);
                }
            }
        }
        return instance;
    }

    public void registerEvents() {
        EventBus.getDefault().register(this);
    }

    public void unregisterEvents() {
        EventBus.getDefault().unregister(this);
        if(dataManager != null){
            dataManager.release();
            dataManager=null;
        }
        instance = null;
    }

    @Subscribe
    public void onEvent(LightDetector.LightEvent lightEvent)
    {
        mHandler.post(logSleep(true));
    }

    @Subscribe
    public void onEvent(MovementDetector.MovementEvent movementEvent)
    {
        mHandler.post(logSleep(true));
    }

    @Subscribe
    public void onEvent(LocationInfoReceiver.LocationEvent locationEvent)
    {
        mHandler.post(logSleep(true));
    }

    @Subscribe
    public void onEvent(ScreenStateReceiver.ScreenStateEvent screenStateEvent)
    {
        if(screenStateEvent == null) {
            return;
        }

        if(!screenStateEvent.isScreenOn()) {
            mHandler.post(updateStartTime());
        }else {
            mHandler.post(logSleep(false));
        }
    }

    private Runnable logSleep(final boolean updateSt) {
        return new Runnable() {
            @Override
            public void run() {
                long st = dataManager.getStartTime();
                long currTime = System.currentTimeMillis();
                if(st != 0l && dataManager.isLargestSleepDuration(currTime - st)) {
                    dataManager.insert(st, currTime);
                    dataManager.saveLastSleepDuration(currTime - st);
                    if(updateSt){
                        dataManager.saveStartTime(System.currentTimeMillis());
                    }
                }
            }
        };
    }

    private Runnable updateStartTime() {
        return new Runnable() {
            @Override
            public void run() {
                dataManager.saveStartTime(System.currentTimeMillis());
            }
        };
    }
}
