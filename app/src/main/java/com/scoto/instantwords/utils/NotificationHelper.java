package com.scoto.instantwords.utils;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.scoto.instantwords.R;
import com.scoto.instantwords.data.database.AppDatabase;
import com.scoto.instantwords.data.model.Word;
import com.scoto.instantwords.ui.NotificationActivity;

import java.util.List;
import java.util.Random;

public class NotificationHelper extends ContextWrapper {

    private NotificationManager notificationManager;
    private NotificationChannel notificationChannel;
    private final String channelID = "default";
    private static final String TAG = "NotificationHelper";

    private AppDatabase appDatabase;
    private List<Word> wordList;

    public NotificationHelper(Context context) {
        super(context);
        appDatabase = AppDatabase.getINSTANCE(context);
        wordList = appDatabase.wordDao().getAllIsReminded();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    public void createChannel() {
        notificationChannel = new NotificationChannel(channelID, "Reminder", NotificationManager.IMPORTANCE_HIGH);
        notificationChannel.enableVibration(true);
        notificationChannel.enableLights(true);
        notificationChannel.setDescription("Daily Word Reminder");
        getNotificationManager().createNotificationChannel(notificationChannel);

    }

    public NotificationManager getNotificationManager() {
        if (notificationManager == null) {
            notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        }
        return notificationManager;
    }

    public NotificationCompat.Builder getChannelNotification() {

        Random rand = new Random();
        int randNum = rand.nextInt(wordList.size());
        String w = wordList.get(randNum).getWord();
        String d = wordList.get(randNum).getDefinition();
        Intent contentIntent = new Intent(this, NotificationActivity.class);
        contentIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, contentIntent, 0);

        //Actions


        return new NotificationCompat.Builder(this, channelID)
                .setSmallIcon(R.drawable.ic_info)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setContentTitle(w)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                .setStyle(new NotificationCompat.BigTextStyle().bigText(d))
                .setAutoCancel(true);
    }

}
