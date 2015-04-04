package com.example.vishwasdamle.quickNote.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;

import com.example.vishwasdamle.quickNote.R;
import com.example.vishwasdamle.quickNote.model.Constants;
import com.example.vishwasdamle.quickNote.model.ExpenseEntry;
import com.example.vishwasdamle.quickNote.repository.ExpenseEntryMapper;

import org.joda.time.DateTime;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static com.example.vishwasdamle.quickNote.model.Constants.EXPORT_REQUEST_CODE;


public class ExportActivity extends ActionBarActivity {

    private ExpenseEntryMapper expenseEntryMapper;

    public ExportActivity() {
        this.expenseEntryMapper = new ExpenseEntryMapper(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);
        int requestCode = getIntent().getIntExtra(Constants.REQUEST_CODE_KEY, EXPORT_REQUEST_CODE);
        if(requestCode == EXPORT_REQUEST_CODE)
            getSupportActionBar().setSubtitle(R.string.Export);
        else
            getSupportActionBar().setSubtitle(R.string.ExportForSharing);
    }

    public void exportEntries(View view) {

        ArrayList<ExpenseEntry> expenseEntries = generateRecords();

        String filename = Constants.FILENAME_PREFIX +  new DateTime().toString(Constants.DATE_TIME_PATTERN);

        if(exportToCSV(expenseEntries, filename)) {
            Intent resultIntent = new Intent();
            String filePath;
            try {
                filePath = getFilesDir().getCanonicalPath() + filename;
            } catch (IOException e) {
                filePath = filename;
            }
            resultIntent.putExtra(Constants.FILENAME_KEY, filePath);
            this.setResult(RESULT_OK, resultIntent);
        }
        System.out.println("exported");
        finish();
    }

    private ArrayList<ExpenseEntry> generateRecords() {
        return expenseEntryMapper.listAll();
    }

    private boolean exportToCSV(ArrayList<ExpenseEntry> expenseEntries, String filename) {


        FileOutputStream fileOutputStream;

        try {
            fileOutputStream = openFileOutput(filename + Constants.CSV_EXTENSION, Context.MODE_PRIVATE);
            for(ExpenseEntry entry : expenseEntries) {
                fileOutputStream.write(entry.getCSVPrintable().getBytes());
            }
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
