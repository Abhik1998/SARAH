package com.triointeli.sarah;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingEvent;
import com.triointeli.sarah.DatabaseModels.Reminder;
import com.triointeli.sarah.DatabaseModels.YourPlaces;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Abhik on 6/2/18.
 */

public class GeofenceTransitionService extends IntentService {

    private static final String TAG = GeofenceTransitionService.class.getSimpleName();

    public static final int GEOFENCE_NOTIFICATION_ID = 0;

    public GeofenceTransitionService() {
        super(TAG);
    }

    Realm realm;

    @Override
    protected void onHandleIntent(Intent intent) {
        realm = Realm.getDefaultInstance();

        Random rn = new Random();

        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);

        // Handling errors
        if (geofencingEvent.hasError()) {
            String errorMsg = getErrorString(geofencingEvent.getErrorCode());
            Log.e(TAG, errorMsg);
            return;
        }

        int geoFenceTransition = geofencingEvent.getGeofenceTransition();

        // Check if the transition type is of interest
        if (geoFenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {

            // Get the geofence that were triggered
            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();

            RealmResults<Reminder> realmReminderResult = realm.where(Reminder.class).equalTo("placeOnEnter", triggeringGeofences.get(0).getRequestId()).findAll();

            // Use an iterator to give all notifs
            realm.beginTransaction();

            for (Reminder rnNDR : realmReminderResult) {
                Intent intentNotify = new Intent();
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
                builder.setSmallIcon(R.drawable.logo_sarah);
                builder.setContentIntent(pendingIntent);
                builder.setAutoCancel(true);
                builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo_sarah));
                builder.setContentTitle("Enterred" + rnNDR.getPlaceOnEnter());
                builder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
                builder.setContentText(rnNDR.getReminderContent());
                NotificationManager notificationManager = (NotificationManager) getSystemService(
                        NOTIFICATION_SERVICE);
                notificationManager.notify(rn.nextInt(50), builder.build());

            }

            realm.commitTransaction();
            //String geofenceTransitionDetails = getGeofenceTransitionDetails(geoFenceTransition, triggeringGeofences );

            // Send notification details as a String
        }

        if (geoFenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {


            // Get the geofence that were triggered
            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();

            RealmResults<Reminder> realmReminderResult = realm.where(Reminder.class).equalTo("placeOnLeave", triggeringGeofences.get(0).getRequestId()).findAll();

            // Use an iterator to give all notifs
            realm.beginTransaction();

            for (Reminder rnNDR : realmReminderResult) {
                Intent intentNotify = new Intent();
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
                builder.setSmallIcon(R.drawable.logo_sarah);
                builder.setContentIntent(pendingIntent);
                builder.setAutoCancel(true);
                builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo_sarah));
                builder.setContentTitle("Left" + rnNDR.getPlaceOnLeave());
                builder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
                builder.setContentText(rnNDR.getReminderContent());
                NotificationManager notificationManager = (NotificationManager) getSystemService(
                        NOTIFICATION_SERVICE);
                notificationManager.notify(rn.nextInt(50), builder.build());

            }
        }
    }

    private static String getErrorString(int errorCode) {
        switch (errorCode) {
            case GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE:
                return "GeoFence not available";
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES:
                return "Too many GeoFences";
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS:
                return "Too many pending intents";
            default:
                return "Unknown error.";
        }
    }
}