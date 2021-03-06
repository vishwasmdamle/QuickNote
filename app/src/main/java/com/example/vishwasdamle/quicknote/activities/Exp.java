package com.example.vishwasdamle.quicknote.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vishwasdamle.quicknote.R;
import com.example.vishwasdamle.quicknote.adaptors.ButtonAdaptor;
import com.example.vishwasdamle.quicknote.model.ExpenseEntry;
import com.example.vishwasdamle.quicknote.model.ExpenseType;
import com.example.vishwasdamle.quicknote.service.AutoCompleteService;
import com.example.vishwasdamle.quicknote.service.ExpenseService;
import com.example.vishwasdamle.quicknote.views.ExpenseTypeSpinner;

import java.io.File;
import java.util.ArrayList;

import static android.R.layout.simple_list_item_1;
import static android.view.View.INVISIBLE;
import static android.view.View.OnClickListener;
import static android.view.View.VISIBLE;
import static android.widget.AdapterView.OnItemClickListener;
import static com.example.vishwasdamle.quicknote.model.Constants.*;
import static com.example.vishwasdamle.quicknote.repository.StoredPreferences.isAutoCompleteEnabled;
import static com.example.vishwasdamle.quicknote.repository.StoredPreferences.isKeyboardEnabled;


public class Exp extends ActionBarActivity implements OnClickListener, OnItemClickListener {

  public static final String SHARE_SUBJECT = "Expense Statement";
  public static final String SHARE_EXTRA_TEXT = "Expense statement generated using QuickNote";
  public static final String SHARE_CHOOSER_TITLE = "Send File via...";
  ExpenseService expenseService;
  AutoCompleteService autoCompleteService;

  public Exp() {
    this.expenseService = new ExpenseService(this);
    this.autoCompleteService = new AutoCompleteService(this);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_exp);
    initAutoCompleteSuggestions();
  }

  private void initNumPad() {
    if (isKeyboardEnabled(this)) {
      GridView gridView = (GridView) findViewById(R.id.numPad);
      ButtonAdaptor buttonAdaptor = new ButtonAdaptor(this);
      gridView.setAdapter(buttonAdaptor);
      gridView.setOnItemClickListener(this);

      gridView.setVisibility(VISIBLE);
      findViewById(R.id.amount).setFocusable(false);
      findViewById(R.id.amount).setFocusableInTouchMode(false);
    } else {
      GridView gridView = (GridView) findViewById(R.id.numPad);
      gridView.setVisibility(INVISIBLE);

      findViewById(R.id.amount).setFocusable(true);
      findViewById(R.id.amount).setFocusableInTouchMode(true);
    }
  }

  private void initAutoCompleteSuggestions() {
    MultiAutoCompleteTextView description = (MultiAutoCompleteTextView) findViewById(R.id.description);
    description.setOnClickListener(this);
    description.setThreshold(1);
    description.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
  }

  private void setupAutoCompleteWords() {
    ArrayAdapter<String> descriptionAdapter;
    if (isAutoCompleteEnabled(this)) {
      descriptionAdapter = new ArrayAdapter<>(this, simple_list_item_1, autoCompleteService.listAll());
    } else {
      descriptionAdapter = new ArrayAdapter<>(this, simple_list_item_1, new ArrayList<String>());
    }
    ((MultiAutoCompleteTextView) findViewById(R.id.description)).setAdapter(descriptionAdapter);

  }

  @Override
  protected void onResume() {
    super.onResume();
    setupAutoCompleteWords();
    initNumPad();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == EXPORT_REQUEST_CODE) {
      processExportResult(data, resultCode);
    }

    if (requestCode == EXPORT_TO_SHARE_REQUEST_CODE) {
      processExportForSharingResult(data, resultCode);
    }
  }

  private void processExportForSharingResult(Intent data, int resultCode) {
    if (resultCode == RESULT_OK) {
      String fileType = data.getStringExtra(FILE_TYPE_KEY);
      String filename = data.getStringExtra(FILENAME_KEY);

      Intent shareIntent = generateFileSharingIntent(fileType, filename);

      startActivityForResult(Intent.createChooser(shareIntent, SHARE_CHOOSER_TITLE), SHARE_REQUEST_CODE);
    }
  }

  private void processExportResult(Intent data, int resultCode) {
    if (resultCode == RESULT_OK) {
      String alert = getResources().getString(R.string.Exported);
      String filename = data.getStringExtra(FILENAME_KEY);
      if (filename != null) {
        alert = alert + " : " + filename;
      }
      Toast.makeText(this, alert, Toast.LENGTH_LONG).show();
    } else {
      Toast.makeText(this, R.string.FailedToExport, Toast.LENGTH_LONG).show();
    }
  }

  private Intent generateFileSharingIntent(String fileType, String filename) {
    Intent shareIntent = new Intent(Intent.ACTION_SEND);
    if (fileType != null && fileType.equals(FILE_TYPE_CSV))
      shareIntent.setType("text/plain");

    shareIntent.putExtra(Intent.EXTRA_SUBJECT, SHARE_SUBJECT);
    shareIntent.putExtra(Intent.EXTRA_TEXT, SHARE_EXTRA_TEXT);
    shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(filename)));
    return shareIntent;
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_exp, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == R.id.export) {
      Intent exportIntent = new Intent(this, ExportActivity.class);
      exportIntent.putExtra(REQUEST_CODE_KEY, EXPORT_REQUEST_CODE);
      startActivityForResult(exportIntent, EXPORT_REQUEST_CODE);
    }

    if (id == R.id.review) {
      Intent exportIntent = new Intent(this, ListActivity.class);
      exportIntent.putExtra(REQUEST_CODE_KEY, LIST_REQUEST_CODE);
      startActivityForResult(exportIntent, LIST_REQUEST_CODE);
    }

    if (id == R.id.share) {
      Intent exportIntent = new Intent(this, ExportActivity.class);
      exportIntent.putExtra(REQUEST_CODE_KEY, EXPORT_TO_SHARE_REQUEST_CODE);
      startActivityForResult(exportIntent, EXPORT_TO_SHARE_REQUEST_CODE);
    }
    if (id == R.id.action_settings) {
      startActivity(new Intent(this, SettingsActivity.class));
    }

    return super.onOptionsItemSelected(item);
  }

  public void save(View view) {
    ExpenseEntry expenseEntry = generateExpenseEntry();
    if (expenseEntry == null) {
      Toast.makeText(this, getString(R.string.fieldErrorMessage), Toast.LENGTH_LONG).show();
      return;
    }
    autoCompleteService.add(expenseEntry.getDescription());
    setupAutoCompleteWords();

    if (expenseService.saveOrUpdate(expenseEntry)) {
      Toast.makeText(this, R.string.Saved, Toast.LENGTH_SHORT).show();
    } else {
      Toast.makeText(this, R.string.FailedToSave, Toast.LENGTH_SHORT).show();
    }
    clearForm();
  }

  private void clearForm() {
    ((TextView) findViewById(R.id.description)).setText("");
    ((TextView) findViewById(R.id.amount)).setText("");
  }

  private ExpenseEntry generateExpenseEntry() {
    ExpenseTypeSpinner expenseType = (ExpenseTypeSpinner) findViewById(R.id.expenseType);
    TextView descriptionTextView = (TextView) findViewById(R.id.description);
    TextView amountTextView = (TextView) findViewById(R.id.amount);

    ExpenseType selectedType = expenseType.getSelection();
    String amountString = String.valueOf(amountTextView.getText());
    String description = String.valueOf(descriptionTextView.getText());
    Double amount;

    if (!amountString.matches(REGEX_DOUBLE) || description.isEmpty()) {
      return null;
    }
    amount = Double.parseDouble(amountString);
    description = description.replaceAll("\\s*,\\s*$", "");

    return new ExpenseEntry(selectedType, amount, description);
  }

  public void backspace(View view) {
    EditText amount = (EditText) findViewById(R.id.amount);
    String text = String.valueOf(amount.getText());
    if (text.length() > 0) amount.setText(text.substring(0, text.length() - 1));
  }

  @Override
  public void onClick(View view) {
    MultiAutoCompleteTextView description = (MultiAutoCompleteTextView) findViewById(R.id.description);
    if (view == description) {
      description.showDropDown();
    }
  }

  @Override
  public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
    GridView gridView = (GridView) findViewById(R.id.numPad);
    EditText amount = (EditText) findViewById(R.id.amount);
    if (adapterView == gridView) {
      amount.setText(amount.getText().toString() + ((Button) view.findViewById(R.id.numPadKey)).getText());
    }
  }
}
