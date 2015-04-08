package com.example.vishwasdamle.quicknote.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.CheckBox;

import com.example.vishwasdamle.quicknote.R;
import com.example.vishwasdamle.quicknote.service.AutoCompleteService;


public class SettingsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setSubtitle(R.string.Settings);
        ((CheckBox)findViewById(R.id.toggleAutoComplete)).setChecked(AutoCompleteService.isEnabled());

    }

    public void toggleAutoComplete(View view) {
        AutoCompleteService.setEnabled(((CheckBox)findViewById(R.id.toggleAutoComplete)).isChecked());
    }
}
