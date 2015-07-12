package com.example.vishwasdamle.quicknote.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ListView;
import android.widget.TextView;

import com.example.vishwasdamle.quicknote.R;
import com.example.vishwasdamle.quicknote.adaptors.ExpenseListAdaptor;
import com.example.vishwasdamle.quicknote.model.ExpenseEntry;
import com.example.vishwasdamle.quicknote.service.ExpenseService;

import java.util.ArrayList;

public class ListActivity extends ActionBarActivity {

  private final ExpenseService expenseService;

  public ListActivity() {
    this.expenseService = new ExpenseService(this);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_list);
    ArrayList<ExpenseEntry> expenseEntries = expenseService.listAll();
    displayEntries(expenseEntries);
    displayBalance(ExpenseEntry.accumulate(expenseEntries).toString());
  }

  private void displayEntries(ArrayList<ExpenseEntry> expenseEntries) {
    ListView listView = (ListView) findViewById(R.id.expenseList);
    ExpenseListAdaptor expenseListAdaptor = new ExpenseListAdaptor(this, expenseEntries);
    listView.setAdapter(expenseListAdaptor);
  }

  private void displayBalance(String balance) {
    ((TextView) findViewById(R.id.balance_amount)).setText(balance);
  }

}
