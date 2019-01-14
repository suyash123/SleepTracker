package com.example.suyash.sleeptracker.entities.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class DBManager {

    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public DBManager(Context c) {
        context = c;
    }

    public DBManager open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(String day, String st, String et) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.DAY_ID, day);
        contentValue.put(DatabaseHelper.START_TIME, st);
        contentValue.put(DatabaseHelper.END_TIME, et);
        database.insertWithOnConflict(DatabaseHelper.TABLE_NAME, null, contentValue, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public ArrayList<String> fetch() {
        String[] columns = new String[] { DatabaseHelper.DAY_ID, DatabaseHelper.START_TIME, DatabaseHelper.END_TIME };
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, columns, null, null, null, null, null);
        ArrayList<String> arrayList = new ArrayList<>();
        if (cursor != null) {
            int dayIndex = cursor.getColumnIndex(DatabaseHelper.DAY_ID);
            int stIndex = cursor.getColumnIndex(DatabaseHelper.START_TIME);
            int etIndex = cursor.getColumnIndex(DatabaseHelper.END_TIME);
            try {
                while (cursor.moveToNext()) {
                    String row = cursor.getString(dayIndex) + " , ST :" + cursor.getString(stIndex) + " , ET :" + cursor.getString(etIndex);
                    arrayList.add(row);
                }
            } finally {
                cursor.close();
            }
        }
        return arrayList;
    }
}
