package com.example.vishwasdamle.quicknote.repository;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.vishwasdamle.quicknote.model.Constants;

/**
 * Created by vishwasdamle on 08/04/15.
 */
public class StoredPreferences {

    private static final String AUTOCOMPLETE_PREFERENCE = "AutoCompletePreference";

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
}
