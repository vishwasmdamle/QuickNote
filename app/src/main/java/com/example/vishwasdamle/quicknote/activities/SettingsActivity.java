package com.example.vishwasdamle.quicknote.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.vishwasdamle.quicknote.R;
import com.example.vishwasdamle.quicknote.service.AutoCompleteService;
import com.example.vishwasdamle.quicknote.service.ExpenseService;

import static com.example.vishwasdamle.quicknote.model.Constants.EXPORT_REQUEST_CODE;
import static com.example.vishwasdamle.quicknote.repository.StoredPreferences.isAutoCompleteEnabled;
import static com.example.vishwasdamle.quicknote.repository.StoredPreferences.isKeyboardEnabled;
import static com.example.vishwasdamle.quicknote.repository.StoredPreferences.setAutoCompleteEnabled;
import static com.example.vishwasdamle.quicknote.repository.StoredPreferences.setKeyboardEnabled;

public class SettingsActivity extends ActionBarActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_settings);
    getSupportActionBar().setSubtitle(R.string.Settings);
    ((CheckBox) findViewById(R.id.toggleAutoComplete)).setChecked(isAutoCompleteEnabled(this));
    ((CheckBox) findViewById(R.id.toggleKeyboard)).setChecked(isKeyboardEnabled(this));

  }

  public void toggleAutoComplete(View view) {
    ((CheckBox) findViewById(R.id.toggleAutoComplete)).toggle();
    setAutoCompleteEnabled(this, ((CheckBox) findViewById(R.id.toggleAutoComplete)).isChecked());
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == EXPORT_REQUEST_CODE) {
      if (resultCode == RESULT_OK) {
        deleteData();
      } else {
        Toast.makeText(this, R.string.FailedToExport, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, R.string.DataNotDeleted, Toast.LENGTH_SHORT).show();
      }
    }
  }

  public void toggleKeyboard(View view) {
    ((CheckBox) findViewById(R.id.toggleKeyboard)).toggle();
    setKeyboardEnabled(this, ((CheckBox) findViewById(R.id.toggleKeyboard)).isChecked());
  }

  public void showDeleteSuggestionsPopup(View view) {
    showACDialog();
  }

  public void deleteSuggestions() {
    new AutoCompleteService(this).deleteAll();
  }

  public void deleteData() {
    new ExpenseService(this).deleteAll();
  }

  public void showDeleteDataPopup(View view) {
    showDataDialog();
  }

  private void showACDialog() {
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

  private void showDataDialog() {
    new AlertDialog.Builder(this)
        .setTitle(R.string.clearDataTitle)
        .setMessage(R.string.clearDataMessage)
        .setPositiveButton(R.string.ButtonExportAndDelete, new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialogInterface, int i) {
            Intent exportIntent = new Intent(getApplicationContext(), ExportActivity.class);
            startActivityForResult(exportIntent, EXPORT_REQUEST_CODE);
          }
        })
        .setNegativeButton(R.string.ButtonNo, new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialogInterface, int i) {
          }
        })
        .setNeutralButton(R.string.ButtonYes, new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialogInterface, int i) {
            deleteData();
          }
        })
        .show();
  }
}
