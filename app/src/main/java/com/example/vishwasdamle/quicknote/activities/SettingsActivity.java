package com.example.vishwasdamle.quicknote.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.CheckBox;

import com.example.vishwasdamle.quicknote.R;

import static com.example.vishwasdamle.quicknote.repository.StoredPreferences.*;

public class SettingsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setSubtitle(R.string.Settings);
        ((CheckBox)findViewById(R.id.toggleAutoComplete)).setChecked(isAutoCompleteEnabled(this));

    }

    public void toggleAutoComplete(View view) {
        setAutoCompleteEnabled(this, ((CheckBox)findViewById(R.id.toggleAutoComplete)).isChecked());
    }
}
