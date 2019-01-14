package com.example.suyash.sleeptracker.events;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

public class MovementDetector implements SensorEventListener {

    private static final Object lock = new Object();
    private static MovementDetector mInstance;

    private SensorManager sensorManager;
    private Sensor accelerometer;

    public static MovementDetector getInstance(Context context) {
        if (mInstance == null) {
            synchronized (lock) {
                if (mInstance == null){
                    mInstance = new MovementDetector(context);
                }
            }
        }
        return mInstance;
    }

    private MovementDetector(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
    }

    public void start() {
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void stop() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        if (sensorEvent.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {

            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            float diff = (float) Math.sqrt(x * x + y * y + z * z);
            if (diff > 0.5) {
                Log.d("MovementDetector", "Device motion detected!!!!");
                EventBus.getDefault().post(new MovementEvent("Motion detected"));
            }

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public class MovementEvent {
        private String movement;

        public MovementEvent(String movement) {
            this.movement = movement;
        }

        public String getMovement() {
            return movement;
        }
    }
}
