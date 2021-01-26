package com.scoto.instantwords.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private int MODE = Context.MODE_PRIVATE;
    private String DEFAULT_COLOR = "DEFAULT_COLOR";
    private Activity activity;

    public SharedPrefManager(Activity activity) {
        this.activity = activity;
        sharedPreferences = activity.getPreferences(MODE);
        editor = sharedPreferences.edit();
    }

    public void setPref(String key, String value) {
        editor.putString(key, value);
        editor.apply();
    }

    public String getPref(String key) {
        return sharedPreferences.getString(key, null);
    }

    public void setDefaultColor(String value) {
        editor.putString(DEFAULT_COLOR, value);
        editor.apply();
    }

    public String getDefaultColor() {
        return sharedPreferences.getString(DEFAULT_COLOR, null);
    }


}
