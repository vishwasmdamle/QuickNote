package com.example.vishwasdamle.quickNote.activities;

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

import com.example.vishwasdamle.quickNote.adaptors.ButtonAdaptor;
import com.example.vishwasdamle.quickNote.R;

import static android.widget.AdapterView.*;


public class
        Exp extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exp);
        Spinner spinner = (Spinner) findViewById(R.id.transactionType);
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
                amount.setText(amount.getText().toString() + ((Button)view.findViewById(R.id.numPadKey)).getText());
            }
        };
        gridView.setOnItemClickListener(numPadListener);
    }

    private void setupAutoCompleteSuggestions(final MultiAutoCompleteTextView description) {
        String[] descriptions = new String[] {"sample1", "sample2", "example1", "example2"};
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
        String[] SpinnerOptions = new String[] {"Debit", "Credit", "Revert"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, SpinnerOptions);
        spinner.setAdapter(adapter);
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

    }
}
