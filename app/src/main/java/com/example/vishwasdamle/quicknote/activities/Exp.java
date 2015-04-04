package com.example.vishwasdamle.quickNote.activities;

import android.content.res.ColorStateList;
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

import java.util.ArrayList;

import static android.widget.AdapterView.*;


public class Exp extends ActionBarActivity {

    public static final String REGEX_DOUBLE = "[-+]?[0-9]*\\.?[0-9]+";
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
        expenseService.save(expenseEntry);
        ArrayList<ExpenseEntry> expenseEntryArrayList = expenseService.listAll();
        for(ExpenseEntry expense : expenseEntryArrayList) {
            System.out.println("expense = " + expense);
            System.out.println("expense.getPrintable() = " + expense.getPrintable());
        }
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
