package com.example.vishwasdamle.quicknote.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.CheckBox;

import com.example.vishwasdamle.quicknote.R;
import com.example.vishwasdamle.quicknote.service.AutoCompleteService;

import static com.example.vishwasdamle.quicknote.repository.StoredPreferences.*;

public class SettingsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setSubtitle(R.string.Settings);
        ((CheckBox)findViewById(R.id.toggleAutoComplete)).setChecked(isAutoCompleteEnabled(this));
        ((CheckBox)findViewById(R.id.toggleKeyboard)).setChecked(isKeyboardEnabled(this));

    }

    public void toggleAutoComplete(View view) {
        ((CheckBox)findViewById(R.id.toggleAutoComplete)).toggle();
        setAutoCompleteEnabled(this, ((CheckBox) findViewById(R.id.toggleAutoComplete)).isChecked());
    }

    public void toggleKeyboard(View view) {
        ((CheckBox)findViewById(R.id.toggleKeyboard)).toggle();
        setKeyboardEnabled(this, ((CheckBox)findViewById(R.id.toggleKeyboard)).isChecked());
    }

    public void showDeleteSuggestionsPopup(View view) {
        showDialog();
    }

    public void deleteSuggestions() {
        new AutoCompleteService(this).deleteAll();
    }

    private void showDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.clearACTitle)
                .setMessage(R.string.clearACMessage)
                .setPositiveButton(R.string.ButtonYes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteSuggestions();
                    }
                })
                .setNegativeButton(R.string.ButtonNo, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .show();
    }
}
