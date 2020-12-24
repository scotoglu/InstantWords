package com.scoto.instantwords.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

public class NotificationReceiver extends BroadcastReceiver {
    private static final String TAG = "NotificationReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {


        if (intent.hasExtra("cancel")) {
            Toast.makeText(context, "Cancel Clicked", Toast.LENGTH_SHORT).show();
        }

        if (intent.getStringExtra("ALARM_INTENT") != null) {
            NotificationHelper notificationHelper = new NotificationHelper(context);
            NotificationCompat.Builder comBuilder = notificationHelper.getChannelNotification();
            notificationHelper.getNotificationManager().notify(1, comBuilder.build());
        }

    }
}
