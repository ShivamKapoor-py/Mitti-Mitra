package com.example.mittimitra;

import android.content.Context;
import android.content.SharedPreferences;

public class Prefs {
    private static final String PREFS_NAME = "mittimitra_prefs";
    private static final String KEY_LANGUAGE = "language";
    private static final String KEY_LOGGED_IN = "logged_in";
    private static final String KEY_SELECTED_CROP = "selected_crop";

    private final SharedPreferences sharedPreferences;

    public Prefs(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void setLanguage(String languageCode) {
        sharedPreferences.edit().putString(KEY_LANGUAGE, languageCode).apply();
    }

    public String getLanguage() {
        return sharedPreferences.getString(KEY_LANGUAGE, null);
    }

    public void setLoggedIn(boolean loggedIn) {
        sharedPreferences.edit().putBoolean(KEY_LOGGED_IN, loggedIn).apply();
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_LOGGED_IN, false);
    }

    public void setSelectedCrop(String crop) {
        sharedPreferences.edit().putString(KEY_SELECTED_CROP, crop).apply();
    }

    public String getSelectedCrop() {
        return sharedPreferences.getString(KEY_SELECTED_CROP, null);
    }
}


