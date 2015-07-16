package com.example.vishwasdamle.quicknote.activities;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.vishwasdamle.quicknote.R;
import com.example.vishwasdamle.quicknote.adaptors.ExpenseListAdaptor;
import com.example.vishwasdamle.quicknote.model.ExpenseEntry;
import com.example.vishwasdamle.quicknote.service.ExpenseService;

import java.util.ArrayList;

public class ListActivity extends ActionBarActivity {

  private final ExpenseService expenseService;
  private ListView expenseList;
  private ArrayList<ExpenseEntry> expenseEntries;

  public ListActivity() {
    this.expenseService = new ExpenseService(this);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_list);
    expenseList = (ListView) findViewById(R.id.expenseList);

    setupData();
    attachLongClickBehavior();
  }

  private void setupData() {
    expenseEntries = getAllExpenseEntries();
    displayEntries(expenseEntries);
    displayBalance(ExpenseEntry.accumulate(expenseEntries).toString());
  }

  private ArrayList<ExpenseEntry> getAllExpenseEntries() {
    return expenseService.listAll();
  }

  @TargetApi(Build.VERSION_CODES.HONEYCOMB)
  private void attachLongClickBehavior() {
    expenseList.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
    expenseList.setMultiChoiceModeListener(new ModeChangeListener());
  }

  private void displayEntries(ArrayList<ExpenseEntry> expenseEntries) {
    ExpenseListAdaptor expenseListAdaptor = new ExpenseListAdaptor(this, expenseEntries);
    expenseList.setAdapter(expenseListAdaptor);
  }

  private void displayBalance(String balance) {
    ((TextView) findViewById(R.id.balance_amount)).setText(balance);
  }

  @TargetApi(Build.VERSION_CODES.HONEYCOMB)
  private class ModeChangeListener implements AbsListView.MultiChoiceModeListener {

    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
      final int checkedCount = expenseList.getCheckedItemCount();
      if (checkedCount == 0) {
        mode.setSubtitle(null);
        mode.setTitle(null);
      } else {
        String selectionSuffix = checkedCount == 1 ?
            getString(R.string.item_selected) : getString(R.string.items_selected);
        mode.setSubtitle(checkedCount + " " + selectionSuffix);
        mode.setTitle(getString(R.string.delete_records));
      }
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
      mode.getMenuInflater().inflate(R.menu.menu_expense_list, menu);
      return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
      return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
      if (item.getItemId() == R.id.list_delete) {
        deleteSelectedEntries();
        setupData();
        mode.finish();
      }
      return true;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
    }
  }

  private void deleteSelectedEntries() {
    SparseBooleanArray checkedStates = expenseList.getCheckedItemPositions();
    for (int index = 0; index < checkedStates.size(); index++) {
      if (checkedStates.valueAt(index)) {
        Long expenseEntryId =
            ((ExpenseEntry) expenseList.getItemAtPosition(checkedStates.keyAt(index))).getUid();
        expenseService.delete(expenseEntryId);
      }
    }
  }
}
