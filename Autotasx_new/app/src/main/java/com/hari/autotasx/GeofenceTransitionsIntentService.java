/**
 * Copyright 2014 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hari.autotasx;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.ArrayList;
import java.util.List;


public class GeofenceTransitionsIntentService extends IntentService {

    protected static final String TAG = "geofence-transitions-service";


    public GeofenceTransitionsIntentService() {

        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            String errorMessage = GeofenceErrorMessages.getErrorString(this,
                    geofencingEvent.getErrorCode());
           // Log.e(TAG, errorMessage);
            return;
        }


        int geofenceTransition = geofencingEvent.getGeofenceTransition();


        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
                geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {


            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();


            String geofenceTransitionDetails = getGeofenceTransitionDetails(
                    this,
                    geofenceTransition,
                    triggeringGeofences
            );


            sendNotification(geofenceTransitionDetails);
            if(ActionFragment.silVar==true)
            {
                Toast.makeText(this,"silent",Toast.LENGTH_SHORT).show();
                // AudioManager am= (AudioManager)getBaseContext().getSystemService(Context.AUDIO_SERVICE);
                AudioManager am= (AudioManager)getBaseContext().getSystemService(Context.AUDIO_SERVICE);
                am.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                //sam.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                //enter into db
            }
            else if(ActionFragment.silVar==false) {
                Toast.makeText(this,"general",Toast.LENGTH_SHORT).show();
                AudioManager am1= (AudioManager) getBaseContext().getSystemService(Context.AUDIO_SERVICE);
                am1.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                //entire null value
            }
            if(ActionFragment.wifiVar==true){
                WifiManager wifiManager = (WifiManager)getBaseContext().getSystemService(Context.WIFI_SERVICE);
                wifiManager.setWifiEnabled(true);
            }
            else if (ActionFragment.wifiVar==false)
            {
                WifiManager wifiManager1 = (WifiManager)getBaseContext().getSystemService(Context.WIFI_SERVICE);
                wifiManager1.setWifiEnabled(false);
            }
            if(ActionFragment.smsVar==true){
                Toast.makeText(this, "inside sendsms()", Toast.LENGTH_SHORT).show();
                SmsManager sm  = SmsManager.getDefault();
                System.out.printf("sm",sm);
                Toast.makeText(this,"sending....",Toast.LENGTH_SHORT).show();
                sm.sendTextMessage("+12149832121", null, "Hi, my android app is sending this message", null, null);
                Toast.makeText(this,"sms sent :)",Toast.LENGTH_SHORT).show();
            }
            else if (ActionFragment.wifiVar==false)
            {
               // WifiManager wifiManager1 = (WifiManager)getBaseContext().getSystemService(Context.WIFI_SERVICE);
               // wifiManager1.setWifiEnabled(false);
            }

           // Log.i(TAG, geofenceTransitionDetails);
        } else {

           // Log.e(TAG, getString(R.string.geofence_transition_invalid_type, geofenceTransition));
        }
    }


    private String getGeofenceTransitionDetails(
            Context context,
            int geofenceTransition,
            List<Geofence> triggeringGeofences) {

        String geofenceTransitionString = getTransitionString(geofenceTransition);


        ArrayList triggeringGeofencesIdsList = new ArrayList();
        for (Geofence geofence : triggeringGeofences) {
            triggeringGeofencesIdsList.add(geofence.getRequestId());
        }
        String triggeringGeofencesIdsString = TextUtils.join(", ",  triggeringGeofencesIdsList);

        return geofenceTransitionString + ": " + triggeringGeofencesIdsString;
    }


    private void sendNotification(String notificationDetails) {
        Toast.makeText(this,"here",Toast.LENGTH_SHORT).show();
        Intent notificationIntent = new Intent(getApplicationContext(), MapsActivity.class);


        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);


        stackBuilder.addParentStack(MapsActivity.class);


        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent notificationPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);


       builder.setSmallIcon(R.drawable.ic_add)

               .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                        R.drawable.ic_add))
                .setColor(Color.RED)
                .setContentTitle(notificationDetails)
                .setContentText(getString(R.string.geofence_transition_notification_text))
                .setContentIntent(notificationPendingIntent);


        builder.setAutoCancel(true);


        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        mNotificationManager.notify(0, builder.build());

    }


    private String getTransitionString(int transitionType) {
        switch (transitionType) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                return getString(R.string.geofence_transition_entered);
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                return getString(R.string.geofence_transition_exited);
            default:
                return getString(R.string.unknown_geofence_transition);
        }
    }
}
