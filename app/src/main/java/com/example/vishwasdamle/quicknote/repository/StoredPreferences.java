package com.example.vishwasdamle.quicknote.repository;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.vishwasdamle.quicknote.model.Constants;

public class StoredPreferences {

  private static final String AUTOCOMPLETE_PREFERENCE = "AutoCompletePreference";
  private static final String KEYBOARD_PREFERENCE = "KeyboardPreference";

  private static SharedPreferences getSharedPreferences(Context context) {
    return context.getSharedPreferences(Constants.STORED_PREFERENCES, Context.MODE_PRIVATE);
  }

  public static boolean isAutoCompleteEnabled(Context context) {
    return getSharedPreferences(context).getBoolean(AUTOCOMPLETE_PREFERENCE, true);
  }

  public static void setAutoCompleteEnabled(Context context, boolean value) {
    getSharedPreferences(context)
        .edit()
        .putBoolean(AUTOCOMPLETE_PREFERENCE, value)
        .commit();
  }

  public static boolean isKeyboardEnabled(Context context) {
    return getSharedPreferences(context).getBoolean(KEYBOARD_PREFERENCE, true);
  }

  public static void setKeyboardEnabled(Context context, boolean value) {
    getSharedPreferences(context)
        .edit()
        .putBoolean(KEYBOARD_PREFERENCE, value)
        .commit();
  }
}
