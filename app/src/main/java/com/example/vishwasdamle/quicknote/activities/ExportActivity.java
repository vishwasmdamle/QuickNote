package com.example.vishwasdamle.quickNote.activities;

import android.content.Intent;
import android.os.storage.StorageManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;

import com.example.vishwasdamle.quickNote.R;
import com.example.vishwasdamle.quickNote.model.ExpenseEntry;
import com.example.vishwasdamle.quickNote.repository.ExpenseEntryMapper;
import com.example.vishwasdamle.quickNote.service.FileService;

import org.joda.time.DateTime;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import static com.example.vishwasdamle.quickNote.model.Constants.*;
import static com.example.vishwasdamle.quickNote.model.Constants.CSV_EXTENSION;
import static com.example.vishwasdamle.quickNote.model.Constants.DATE_TIME_PATTERN;
import static com.example.vishwasdamle.quickNote.model.Constants.EXPORT_REQUEST_CODE;
import static com.example.vishwasdamle.quickNote.model.Constants.FILENAME_PREFIX;


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
        if(requestCode == EXPORT_REQUEST_CODE)
            getSupportActionBar().setSubtitle(R.string.Export);
        else
            getSupportActionBar().setSubtitle(R.string.ExportForSharing);
    }

    public void exportEntries(View view) {
        ArrayList<ExpenseEntry> expenseEntries = generateRecords();

        String filename = FILENAME_PREFIX +  new DateTime().toString(DATE_TIME_PATTERN);
        if(exportToCSV(expenseEntries, filename)) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra(FILENAME_KEY, filename);
            resultIntent.putExtra(FILE_TYPE_KEY, FILE_TYPE_CSV);
            this.setResult(RESULT_OK, resultIntent);
        }
        System.out.println("exported");
        finish();
    }

    private ArrayList<ExpenseEntry> generateRecords() {
        return expenseEntryMapper.listAll();
    }

    private boolean exportToCSV(ArrayList<ExpenseEntry> expenseEntries, String filename) {
        StringBuilder fileContent = new StringBuilder();
        for(ExpenseEntry entry : expenseEntries) {
            fileContent.append(entry.getCSVPrintable());
        }

        File file = fileService.getFile(CSV_DIRECTORY, filename + CSV_EXTENSION);
        if(file == null) return false;
        return fileService.writeToFile(file, fileContent.toString());
    }
}
