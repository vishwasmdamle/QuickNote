package com.example.vishwasdamle.quicknote.activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.vishwasdamle.quicknote.R;
import com.example.vishwasdamle.quicknote.adaptors.ExpenseListAdaptor;
import com.example.vishwasdamle.quicknote.service.ExpenseService;


public class ListActivity extends ActionBarActivity {

    private final ExpenseService expenseService;

    public ListActivity() {
        this.expenseService = new ExpenseService(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ListView listView = (ListView) findViewById(R.id.expenseList);
        ExpenseListAdaptor expenseListAdaptor = new ExpenseListAdaptor(this, expenseService.listAll());
        listView.setAdapter(expenseListAdaptor);
    }

}
