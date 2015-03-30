package com.hari.autotasx;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;


/**
 *  Provides CRUD methods required for managing location objects.
 *
 */
    public class ManageDB
    {

        private static final String TAG = ManageDB.class.getSimpleName();

        // -- Database objects
        //SharedPreferences prefs;
        private SQLiteDatabase database;
        private DatabaseHelper dbHelper;



        public ManageDB(Context context)
        {
            dbHelper = new DatabaseHelper(context);
        }

        public void open() throws SQLException
        {
            database = dbHelper.getWritableDatabase();
        }

        public void close()
        {
            dbHelper.close();
        }


        public void addLocation(GeoFence geoFence)
        {
            ContentValues values = new ContentValues();
            values.put("NAME", geoFence.getNameLoc());
            values.put("LATITUDE", 0);
            values.put("LONGITUDE", 0);
            values.put("RADIUS", 0);
            values.put("SMS",ActionFragment.smsVar);
            values.put("WIFI",ActionFragment.wifiVar);
            values.put("SILENT", ActionFragment.silVar);
            values.put("REMINDER",ActionFragment.remVar);
            values.put("MESSAGE",geoFence.getRemmsg());

            long insertId = database.insert("LOCATION", null, values);

            //Log.i(TAG, "Exiting createProfile(), id after row insertion - " + insertId);
            //Log.i(TAG, "ActionFregamnet sms " + ActionFragment.smsVar);
        }


        //Print a particular row from a Database
        public Cursor getEntry(GeoFence geoFence) {
           Cursor tmpCursor = database.query("LOCATION", new String[] {"_id", "NAME","LATITUDE","LONGITUDE", "RADIUS", "SMS", "WIFI","SILENT", "REMINDER","MESSAGE" }, "NAME" + " = '" + geoFence.getNameLoc() + "'" ,null, null, null,null, null);
            tmpCursor.moveToFirst();
            return tmpCursor;
        }

        //Update a particular row from a Database
        public boolean updateEntry(GeoFence geoFence) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("LATITUDE", geoFence.getPoint().latitude);
            contentValues.put("LONGITUDE", geoFence.getPoint().longitude);
            contentValues.put("RADIUS", geoFence.getRadius());
            return database.update("LOCATION", contentValues, "NAME" + " = '" + geoFence.getNameLoc() + "'",    null) > 0;
        }

    }
