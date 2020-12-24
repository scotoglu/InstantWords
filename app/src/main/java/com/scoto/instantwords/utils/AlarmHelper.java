package com.scoto.instantwords.utils;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.scoto.instantwords.R;
import com.scoto.instantwords.data.model.Word;

import java.util.ArrayList;
import java.util.Calendar;

public class AlarmHelper {

    private AlarmManager alarmManager;
    private Activity activity;
    private SharedPrefHelper sharedPrefManager;
    private PendingIntent pendingIntent;
    private final int ALARM_TYPE = AlarmManager.RTC_WAKEUP;
    private static final String TAG = "AlarmHelper";
    private ArrayList<Word> arrayList;

    public AlarmHelper(Activity activity, ArrayList<Word> words) {
        this.activity = activity;
        this.arrayList = words;
        if (alarmManager == null) {
            alarmManager = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
        }
        sharedPrefManager = new SharedPrefHelper(activity);
        Log.d(TAG, "AlarmHelper: Constructor");
    }


    public void setAlarm() {

        Intent intent = new Intent(activity.getApplicationContext(), NotificationReceiver.class);
        intent.putExtra("ALARM_INTENT", "ALARM_INTENT");
        pendingIntent = PendingIntent.getBroadcast(activity.getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        int hour = sharedPrefManager.getPrefHourAndMinute(activity.getString(R.string.hour));
        int minute = sharedPrefManager.getPrefHourAndMinute(activity.getString(R.string.minute));

        if (hour == -1 || minute == -1) {
            hour = 21;
            minute = 00;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        alarmManager.setRepeating(ALARM_TYPE, calendar.getTimeInMillis(), 1000 * 60, pendingIntent);//One minute
    }

    public void cancelAlarm() {
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }
}
