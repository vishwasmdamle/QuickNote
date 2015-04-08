package com.example.vishwasdamle.quicknote.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vishwasdamle.quicknote.adaptors.ButtonAdaptor;
import com.example.vishwasdamle.quicknote.R;
import com.example.vishwasdamle.quicknote.model.ExpenseEntry;
import com.example.vishwasdamle.quicknote.model.ExpenseType;
import com.example.vishwasdamle.quicknote.service.AutoCompleteService;
import com.example.vishwasdamle.quicknote.service.ExpenseService;

import java.io.File;
import java.util.ArrayList;

import static android.R.layout.*;
import static android.view.View.*;
import static android.widget.AdapterView.*;
import static com.example.vishwasdamle.quicknote.model.Constants.*;
import static com.example.vishwasdamle.quicknote.model.ExpenseType.*;


public class Exp extends ActionBarActivity implements OnClickListener, OnItemClickListener {

    public static final String SHARE_SUBJECT = "Expense Statement";
    public static final String SHARE_EXTRA_TEXT = "Expense statement generated using QuickNote";
    public static final String SHARE_CHOOSER_TITLE = "Send File via...";
    private ButtonAdaptor buttonAdaptor;
    private ArrayAdapter<String> descriptionAdapter;
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
        initSpinner();
        initNumPad();
        initAutoCompleteSuggestions();
    }

    private void initNumPad() {
        GridView gridView = (GridView) findViewById(R.id.numPad);
        buttonAdaptor = new ButtonAdaptor(this);
        gridView.setAdapter(buttonAdaptor);
        gridView.setOnItemClickListener(this);
    }

    private void initAutoCompleteSuggestions() {
        MultiAutoCompleteTextView description = (MultiAutoCompleteTextView) findViewById(R.id.description);
        descriptionAdapter = new ArrayAdapter<>(this, simple_list_item_1);

        description.setAdapter(descriptionAdapter);
        description.setOnClickListener(this);
        description.setThreshold(1);
        description.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
    }

    private void setupAutoCompleteWords() {
        descriptionAdapter.clear();
        if(AutoCompleteService.isEnabled()) {
            ArrayList<String> descriptionList = autoCompleteService.listAll();
            for(String descriptionElement : descriptionList) {
                descriptionAdapter.add(descriptionElement);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupAutoCompleteWords();
    }

    private void initSpinner() {
        Spinner spinner = (Spinner) findViewById(R.id.expenseType);
        ArrayList<String> SpinnerOptions = getStringValues(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, simple_spinner_dropdown_item, SpinnerOptions);
        spinner.setAdapter(adapter);
        String debitStringValue = getResources().getString(DEBIT.getStringId());
        spinner.setSelection(adapter.getPosition(debitStringValue), false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == EXPORT_REQUEST_CODE) {
            processExportResult(data, resultCode);
        }

        if(requestCode == EXPORT_TO_SHARE_REQUEST_CODE) {
            processExportForSharingResult(data, resultCode);
        }
    }

    private void processExportForSharingResult(Intent data, int resultCode) {
        if(resultCode == RESULT_OK) {
            String fileType = data.getStringExtra(FILE_TYPE_KEY);
            String filename = data.getStringExtra(FILENAME_KEY);

            Intent shareIntent = generateFileSharingIntent(fileType, filename);

            startActivityForResult(Intent.createChooser(shareIntent, SHARE_CHOOSER_TITLE), SHARE_REQUEST_CODE);
        }
    }

    private void processExportResult(Intent data, int resultCode) {
        if(resultCode == RESULT_OK) {
            String alert = getResources().getString(R.string.Exported);
            String filename = data.getStringExtra(FILENAME_KEY);
            if(filename != null) {
                alert = alert + " : " + filename;
            }
            Toast.makeText(this, alert, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, R.string.FailedToExport, Toast.LENGTH_LONG).show();
        }
    }

    private Intent generateFileSharingIntent(String fileType, String filename) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        if(fileType != null && fileType.equals(FILE_TYPE_CSV))
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
        if(id == R.id.export) {
            Intent exportIntent = new Intent(this, ExportActivity.class);
            exportIntent.putExtra(REQUEST_CODE_KEY, EXPORT_REQUEST_CODE);
            startActivityForResult(exportIntent, EXPORT_REQUEST_CODE);
        }

        if(id == R.id.review) {
            Intent exportIntent = new Intent(this, ListActivity.class);
            exportIntent.putExtra(REQUEST_CODE_KEY, LIST_REQUEST_CODE);
            startActivityForResult(exportIntent, LIST_REQUEST_CODE);
        }

        if(id == R.id.share) {
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
        if(expenseEntry == null) {
            Toast.makeText(this, getString(R.string.fieldErrorMessage), Toast.LENGTH_LONG).show();
            return;
        }
        autoCompleteService.add(expenseEntry.getDescription());
        setupAutoCompleteWords();

        if(expenseService.save(expenseEntry)) {
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
        Spinner expenseType = (Spinner) findViewById(R.id.expenseType);
        TextView descriptionTextView = (TextView) findViewById(R.id.description);
        TextView amountTextView = (TextView) findViewById(R.id.amount);

        ExpenseType selectedType = values()[expenseType.getSelectedItemPosition()];
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
        if(text.length() > 0) amount.setText(text.substring(0, text.length() - 1));
    }

    @Override
    public void onClick(View view) {
        MultiAutoCompleteTextView description = (MultiAutoCompleteTextView) findViewById(R.id.description);
        if(view == description) {
            description.showDropDown();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        GridView gridView = (GridView) findViewById(R.id.numPad);
        EditText amount = (EditText) findViewById(R.id.amount);
        if(adapterView == gridView) {
            amount.setText(amount.getText().toString() + ((Button) view.findViewById(R.id.numPadKey)).getText());
        }
    }
}
