package com.example.vishwasdamle.quicknote.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vishwasdamle.quicknote.R;
import com.example.vishwasdamle.quicknote.model.ExpenseEntry;
import com.example.vishwasdamle.quicknote.repository.ExpenseEntryMapper;
import com.example.vishwasdamle.quicknote.service.FileService;

import org.joda.time.DateTime;

import java.io.File;
import java.util.ArrayList;

import static com.example.vishwasdamle.quicknote.model.Constants.CSV_EXTENSION;
import static com.example.vishwasdamle.quicknote.model.Constants.DATE_TIME_PATTERN_FILE;
import static com.example.vishwasdamle.quicknote.model.Constants.EXPORT_REQUEST_CODE;
import static com.example.vishwasdamle.quicknote.model.Constants.FILENAME_KEY;
import static com.example.vishwasdamle.quicknote.model.Constants.FILENAME_PREFIX;
import static com.example.vishwasdamle.quicknote.model.Constants.FILE_TYPE_CSV;
import static com.example.vishwasdamle.quicknote.model.Constants.FILE_TYPE_KEY;
import static com.example.vishwasdamle.quicknote.model.Constants.REQUEST_CODE_KEY;


public class ExportActivity extends ActionBarActivity {

  public static final String CSV_DIRECTORY = "/QuickNote/csv/";
  private ExpenseEntryMapper expenseEntryMapper;
  private FileService fileService;

  public ExportActivity() {
    this.expenseEntryMapper = new ExpenseEntryMapper(this);
    this.fileService = new FileService(this);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_export);
    int requestCode = getIntent().getIntExtra(REQUEST_CODE_KEY, EXPORT_REQUEST_CODE);
    if (requestCode == EXPORT_REQUEST_CODE)
      getSupportActionBar().setSubtitle(R.string.Export);
    else
      getSupportActionBar().setSubtitle(R.string.ExportForSharing);
    String filename = FILENAME_PREFIX + new DateTime().toString(DATE_TIME_PATTERN_FILE);
    ((EditText) findViewById(R.id.filename)).setText(filename);
  }

  public void exportEntries(View view) {
    String filename = ((EditText) findViewById(R.id.filename)).getText().toString();
    if (filename.equals("")) {
      Toast.makeText(this, R.string.fieldErrorMessage, Toast.LENGTH_SHORT).show();
    } else {
      filename = filename.concat(CSV_EXTENSION);
      if (fileService.exists(CSV_DIRECTORY, filename)) {
        Toast.makeText(this, R.string.FileExists, Toast.LENGTH_SHORT).show();
      } else {
        ArrayList<ExpenseEntry> expenseEntries = generateRecords();
        File file = exportToCSV(expenseEntries, filename);
        if (file != null) {
          Intent resultIntent = new Intent();
          resultIntent.putExtra(FILENAME_KEY, file.getPath());
          resultIntent.putExtra(FILE_TYPE_KEY, FILE_TYPE_CSV);
          this.setResult(RESULT_OK, resultIntent);
        }
        finish();
      }
    }
  }

  private ArrayList<ExpenseEntry> generateRecords() {
    return expenseEntryMapper.listAll();
  }

  private File exportToCSV(ArrayList<ExpenseEntry> expenseEntries, String filename) {
    StringBuilder fileContent = new StringBuilder();
    for (ExpenseEntry entry : expenseEntries) {
      fileContent.append(entry.getCSVPrintable());
    }

    File file = fileService.getFile(CSV_DIRECTORY, filename);
    if (file == null) return null;
    if (!fileService.writeToFile(file, fileContent.toString())) return null;
    return file;
  }
}
