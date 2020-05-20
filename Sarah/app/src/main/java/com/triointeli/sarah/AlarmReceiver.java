package com.triointeli.sarah;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Abhik on 07-02-2018.
 */

public class AlarmReceiver extends BroadcastReceiver {
    private NotificationManager alarmNotificationManager;
    public static Ringtone ringtone;
    public static CountDownTimer countDownTimer;
    public static Vibrator v;
    private static final String NOTIFICATION_MSG = "NOTIFICATION MSG";

    @Override
    public void onReceive(Context context, Intent intentIn) {

        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(NOTIFICATION_MSG, intentIn.getExtras().getString("CONTENT"));

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent notificationPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context);
        notificationBuilder
                .setSmallIcon(R.drawable.logo_sarah)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.logo_sarah))
                .setColor(Color.RED)
                .setContentTitle("SARAH")
                .setContentText(intentIn.getExtras().getString("CONTENT"))
                .setContentIntent(notificationPendingIntent)
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                .setVibrate(new long[]{1000})
                .setAutoCancel(true);

        //Creating and sending Notification
        NotificationManager notificatioMng =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificatioMng.notify(
                0,
                notificationBuilder.build());


    }


}