package com.example.vishwasdamle.quicknote.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.vishwasdamle.quicknote.model.ExpenseType;

import java.util.ArrayList;

import static android.R.layout.simple_spinner_dropdown_item;
import static com.example.vishwasdamle.quicknote.model.ExpenseType.DEBIT;

public class ExpenseTypeSpinner extends Spinner {

  private final ArrayAdapter<String> adapter;

  public ExpenseTypeSpinner(Context context) {
    super(context);
    adapter = setupAdapter(context);
    setSelection(DEBIT);
  }

  public ExpenseTypeSpinner(Context context, AttributeSet attrs) {
    super(context, attrs);
    adapter = setupAdapter(context);
    setSelection(DEBIT);
  }

  public ExpenseType getSelection() {
    return ExpenseType.values()[getSelectedItemPosition()];
  }

  public void setSelection(ExpenseType expenseType) {
    String debitStringValue = getResources().getString(expenseType.getStringId());
    setSelection(adapter.getPosition(debitStringValue), false);
  }

  private ArrayAdapter<String> setupAdapter(Context context) {
    ArrayList<String> SpinnerOptions = ExpenseType.getStringValues(context);
    ArrayAdapter<String> adapter = new ArrayAdapter<>(context,
        simple_spinner_dropdown_item, SpinnerOptions);
    setAdapter(adapter);
    return adapter;
  }
}
