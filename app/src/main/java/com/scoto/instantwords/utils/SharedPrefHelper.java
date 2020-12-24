package com.scoto.instantwords.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefHelper {

    private Activity activity;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private int mode = Context.MODE_PRIVATE;

    public SharedPrefHelper(Activity activity) {
        this.activity = activity;
        sharedPreferences = activity.getPreferences(mode);
        editor = sharedPreferences.edit();
    }

    public void setPref(String key, String value) {
        editor.putString(key, value);
        editor.apply();
    }

    public String getPref(String key) {
        return sharedPreferences.getString(key, "0");
    }

    public int getPrefHourAndMinute(String key) {
        return sharedPreferences.getInt(key, -1);
    }

    public void setPrefHourAndMinute(String key, int value) {
        editor.putInt(key, value);
        editor.apply();
    }

}
