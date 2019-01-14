package com.example.suyash.sleeptracker.entities.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "TRACKER";

    public static final String DAY_ID = "day_id";
    public static final String START_TIME = "start_time";
    public static final String END_TIME = "end_time";

    static final String DB_NAME = "SLEEP_TRACKER.DB";

    static final int DB_VERSION = 1;

    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" + DAY_ID
            + " TEXT PRIMARY KEY NOT NULL, " + START_TIME + " TEXT NOT NULL, " + END_TIME + " TEXT NOT NULL);";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}