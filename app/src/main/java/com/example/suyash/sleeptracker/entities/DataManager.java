package com.example.suyash.sleeptracker.entities;

import android.content.Context;

import com.example.suyash.sleeptracker.entities.db.DBManager;
import com.example.suyash.sleeptracker.entities.pref.SleepSharedPreference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class DataManager {

    private static final String ST_KEY = "st_key";
    private static final String LAST_SLEEP_DURATION_KEY = "last_sleep_key";
    private static final String DATE_FORMAT = "dd/MM/yyyy";
    private static final String TIME_FORMAT = "hh:mm aa";
    private DBManager dbManager;
    private SleepSharedPreference sharedPreference;

    public DataManager(Context context) {
        dbManager = new DBManager(context).open();
        sharedPreference = new SleepSharedPreference(context);
    }

    public void insert(long startTime, long endTime) {
        String stDay = getDate(startTime);
        dbManager.insert(stDay, getTime(startTime), getTime(endTime));
        removeStartTime();
    }

    public ArrayList<String> getSleepLogs() {
        return dbManager.fetch();
    }

    public void saveStartTime(long time) {
        sharedPreference.saveData(ST_KEY, time);
    }

    private void removeStartTime() {
        sharedPreference.removeData(ST_KEY);
    }

    public long getStartTime() {
        return sharedPreference.getData(ST_KEY, 0l);
    }

    public void saveLastSleepDuration(long duration) {
        sharedPreference.saveData(LAST_SLEEP_DURATION_KEY, duration);
    }

    public boolean isLargestSleepDuration(long currLargestDuration) {
        long lastLargestDuration = sharedPreference.getData(LAST_SLEEP_DURATION_KEY, 0l);
        if(currLargestDuration > lastLargestDuration) {
            return true;
        }
        return false;
    }

    private String getDate(long milliSeconds)
    {
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    private String getTime(long milliSeconds)
    {
        SimpleDateFormat formatter = new SimpleDateFormat(TIME_FORMAT);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public void release() {
        if(dbManager != null) {
            dbManager.close();
            dbManager = null;
        }
        if(sharedPreference != null){
            sharedPreference = null;
        }
    }
}
