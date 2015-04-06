package com.example.vishwasdamle.quickNote.activities;

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

import com.example.vishwasdamle.quickNote.adaptors.ButtonAdaptor;
import com.example.vishwasdamle.quickNote.R;
import com.example.vishwasdamle.quickNote.model.ExpenseEntry;
import com.example.vishwasdamle.quickNote.model.ExpenseType;
import com.example.vishwasdamle.quickNote.service.ExpenseService;

import java.io.File;
import java.util.ArrayList;

import static android.widget.AdapterView.*;
import static com.example.vishwasdamle.quickNote.model.Constants.*;


public class Exp extends ActionBarActivity {

    public static final String SHARE_SUBJECT = "Expense Statement";
    public static final String SHARE_EXTRA_TEXT = "Expense statement generated using QuickNote";
    public static final String SHARE_CHOOSER_TITLE = "Send File via...";
    private static final int SHARE_REQUEST_CODE = 3;
    ExpenseService expenseService;

    public Exp() {
        this.expenseService = new ExpenseService(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exp);
        Spinner spinner = (Spinner) findViewById(R.id.expenseType);
        GridView gridView = (GridView) findViewById(R.id.numPad);
        MultiAutoCompleteTextView description = (MultiAutoCompleteTextView) findViewById(R.id.description);
        initSpinner(spinner);
        initNumPad(gridView);
        setupAutoCompleteSuggestions(description);
    }

    private void initNumPad(GridView gridView) {
        ButtonAdaptor buttonAdaptor = new ButtonAdaptor(this);
        gridView.setAdapter(buttonAdaptor);

        OnItemClickListener numPadListener = new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                EditText amount = (EditText) findViewById(R.id.amount);
                amount.setText(amount.getText().toString() + ((Button) view.findViewById(R.id.numPadKey)).getText());
            }
        };
        gridView.setOnItemClickListener(numPadListener);
    }

    private void setupAutoCompleteSuggestions(final MultiAutoCompleteTextView description) {
        String[] descriptions = new String[]{"sample1", "sample2", "example1", "example2"};
        ArrayAdapter<String> descriptionAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, descriptions);
        description.setAdapter(descriptionAdapter);
        description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                description.showDropDown();
            }
        });
        description.setThreshold(1);
        description.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
    }

    private void initSpinner(Spinner spinner) {
        ArrayList<String> SpinnerOptions = ExpenseType.getStringValues(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, SpinnerOptions);
        spinner.setAdapter(adapter);
        String debitStringValue = getResources().getString(ExpenseType.DEBIT.getStringId());
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_exp, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        System.out.println("item = [" + item + "]");
        System.out.println("item.getTitle() = " + item.getTitle());

        if(id == R.id.export) {
            System.out.println("export");
            Intent exportIntent = new Intent(this, ExportActivity.class);
            exportIntent.putExtra(REQUEST_CODE_KEY, EXPORT_REQUEST_CODE);
            startActivityForResult(exportIntent, EXPORT_REQUEST_CODE);
        }

        if(id == R.id.share) {
            System.out.println("share");
            Intent exportIntent = new Intent(this, ExportActivity.class);
            exportIntent.putExtra(REQUEST_CODE_KEY, EXPORT_TO_SHARE_REQUEST_CODE);
            startActivityForResult(exportIntent, EXPORT_TO_SHARE_REQUEST_CODE);
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void save(View view) {
        ExpenseEntry expenseEntry = generateExpenseEntry();
        if(expenseEntry == null) {
            return;
        }
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
        Toast toast = Toast.makeText(this, getString(R.string.fieldErrorMessage), Toast.LENGTH_LONG);

        ExpenseType selectedType = ExpenseType.values()[expenseType.getSelectedItemPosition()];
        String amountString = String.valueOf(amountTextView.getText());
        Double amount;
        if(amountString.matches(REGEX_DOUBLE)) {
            amount = Double.parseDouble(amountString);
        } else {
            toast.show();
            return null;
        }
        String description = String.valueOf(descriptionTextView.getText());
        if(!description.isEmpty()) {
            description = description.replaceAll("\\s*,\\s*$", "");
        } else {
            toast.show();
            return null;
        }
        return new ExpenseEntry(selectedType, amount, description);
    }

    public void backspace(View view) {
        EditText amount = (EditText) findViewById(R.id.amount);
        String text = String.valueOf(amount.getText());
        if(text.length() > 0) amount.setText(text.substring(0, text.length() - 1));
    }
}
