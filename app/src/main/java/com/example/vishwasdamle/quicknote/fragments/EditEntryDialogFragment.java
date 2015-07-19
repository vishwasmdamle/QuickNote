package com.example.vishwasdamle.quicknote.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.vishwasdamle.quicknote.R;
import com.example.vishwasdamle.quicknote.model.ExpenseEntry;
import com.example.vishwasdamle.quicknote.service.ExpenseService;
import com.example.vishwasdamle.quicknote.views.ExpenseTypeSpinner;

public class EditEntryDialogFragment extends DialogFragment {

  public static final String ENTRY_ID = "ENTRY_ID";
  public static String TAG = "EditEntryDialogFragmentTag";
  private ExpenseService expenseService;
  private Context context;

  public static DialogFragment newInstance(Long entryId) {
    Bundle bundle = new Bundle();
    bundle.putLong(ENTRY_ID, entryId);
    EditEntryDialogFragment editEntryDialogFragment = new EditEntryDialogFragment();
    editEntryDialogFragment.setArguments(bundle);
    return editEntryDialogFragment;
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    getDialog().setTitle(R.string.edit_entry);
    context = getActivity().getApplicationContext();
    View view = inflater.inflate(R.layout.edit_entry_layout, container, false);
    initialiseView(view);
    return view;
  }

  private void initialiseView(View view) {
    ExpenseEntry expenseEntry = getExpenseEntry();
    ((TextView) view.findViewById(R.id.description)).setText(expenseEntry.getDescription());
    ((TextView) view.findViewById(R.id.amount)).setText(expenseEntry.getAmount().toString());
    ((ExpenseTypeSpinner) view.findViewById(R.id.expenseType))
        .setSelection(expenseEntry.getExpenseType());
  }

  private ExpenseEntry getExpenseEntry() {
    expenseService = new ExpenseService(context);
    long entryId = getArguments().getLong(ENTRY_ID);
    return expenseService.get(entryId);
  }
}
