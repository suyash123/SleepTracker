package com.example.suyash.sleeptracker.events;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

public class LightDetector implements SensorEventListener {

    private static final Object lock = new Object();

    private static LightDetector mInstance;
    private SensorManager sensorManager;
    private Sensor lightSensor;
    private float prevVal = -1f;

    public static LightDetector getInstance(Context context) {
        if (mInstance == null) {
            synchronized (lock) {
                if (mInstance == null){
                    mInstance = new LightDetector(context);
                }
            }
        }
        return mInstance;
    }

    private LightDetector(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
    }

    public void start() {
        sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void stop() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        if(sensorEvent.sensor.getType() == Sensor.TYPE_LIGHT){
            float val = sensorEvent.values[0];
            Log.d("LightDetector", "Light change event received. " + val);
            if(prevVal != -1f) {
                float diff = prevVal > val ? prevVal - val : val - prevVal;
                if(diff > 20f) {
                    EventBus.getDefault().post(new LightEvent("Light changed."));
                }
            }
            prevVal = val;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public class LightEvent {
        private String lightChangeMsg;

        public LightEvent(String lightChangeMsg) {
            this.lightChangeMsg = lightChangeMsg;
        }

        public String getLightChangeMsg() {
            return lightChangeMsg;
        }
    }
}
