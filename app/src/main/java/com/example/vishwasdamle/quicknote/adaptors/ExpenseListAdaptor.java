package com.example.vishwasdamle.quicknote.adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.vishwasdamle.quicknote.R;
import com.example.vishwasdamle.quicknote.model.Constants;
import com.example.vishwasdamle.quicknote.model.ExpenseEntry;
import com.example.vishwasdamle.quicknote.model.ExpenseType;

import java.util.ArrayList;

public class ExpenseListAdaptor extends BaseAdapter {
  private Context context;
  private ArrayList<ExpenseEntry> expenseEntryList;

  public ExpenseListAdaptor(Context context, ArrayList<ExpenseEntry> expenseEntryList) {
    this.context = context;
    this.expenseEntryList = expenseEntryList;
  }

  @Override
  public int getCount() {
    return expenseEntryList.size();
  }

  @Override
  public Object getItem(int i) {
    return expenseEntryList.get(i);
  }

  @Override
  public long getItemId(int i) {
    return expenseEntryList.get(i).getUid();
  }

  @Override
  public View getView(int i, View view, ViewGroup viewGroup) {
    if (view == null) {
      view = LayoutInflater.from(context).inflate(R.layout.list_layout, viewGroup, false);
    }
    ExpenseEntry currentExpense = expenseEntryList.get(i);

    buildView(view, currentExpense);
    return view;
  }

  private void buildView(final View view, ExpenseEntry currentExpense) {
    TextView expenseTypeText = (TextView) view.findViewById(R.id.type);
    if (currentExpense.getExpenseType() == ExpenseType.DEBIT) {
      expenseTypeText.setTextColor(context.getResources().getColor(R.color.red_light));
    } else if (currentExpense.getExpenseType() == ExpenseType.CREDIT) {
      expenseTypeText.setTextColor(context.getResources().getColor(R.color.green_light));
    } else {
      expenseTypeText.setTextColor(context.getResources().getColor(android.R.color.primary_text_dark));
    }

    String expenseType = String.valueOf(currentExpense.getExpenseType().toString().charAt(0));
    ((TextView) view.findViewById(R.id.type)).setText(expenseType);
    ((TextView) view.findViewById(R.id.amount)).setText(currentExpense.getAmount().toString());
    ((TextView) view.findViewById(R.id.description)).setText(currentExpense.getDescription());
    ((TextView) view.findViewById(R.id.timestamp)).setText(currentExpense.getTimeStamp().toString(Constants.DATE_TIME_PATTERN_DISPLAY));
  }
}
