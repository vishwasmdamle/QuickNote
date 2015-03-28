package com.example.vishwasdamle.quickNote;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;


public class
        Exp extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exp);
        Spinner spinner = (Spinner) findViewById(R.id.transactionType);
        MultiAutoCompleteTextView description = (MultiAutoCompleteTextView) findViewById(R.id.description);
        initSpinner(spinner);
        setupAutoCompleteSuggestions(description);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
