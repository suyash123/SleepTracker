package com.example.suyash.sleeptracker.entities.pref;

import android.content.Context;
import android.content.SharedPreferences;

public class SleepSharedPreference {

    private static final String PREF_NAME = "sleep_pref";
    private SharedPreferences sharedPreferences;

    public SleepSharedPreference(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
    }

    public void saveData(String key,long value) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putLong(key, value);
        prefsEditor.apply();
    }

    public long getData(String key, long defaultValue) {
        if (sharedPreferences!= null) {
            return sharedPreferences.getLong(key, defaultValue);
        }
        return defaultValue;
    }

    public void removeData(String key){
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.remove(key);
        prefsEditor.apply();
    }

    public void saveData(String key,String value) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putString(key, value);
        prefsEditor.apply();
    }

    public String getData(String key, String defaultValue) {
        if (sharedPreferences!= null) {
            return sharedPreferences.getString(key, defaultValue);
        }
        return defaultValue;
    }
}
