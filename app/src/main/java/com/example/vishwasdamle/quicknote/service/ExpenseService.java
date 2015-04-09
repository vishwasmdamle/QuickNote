package com.example.vishwasdamle.quicknote.service;

import android.content.Context;

import com.example.vishwasdamle.quicknote.model.ExpenseEntry;
import com.example.vishwasdamle.quicknote.repository.ExpenseEntryMapper;

import java.util.ArrayList;

/**
 * Created by vishwasdamle on 03/04/15.
 */
public class ExpenseService {
    ExpenseEntryMapper expenseEntryMapper;
    Context context;

    public ExpenseService(Context context) {
        this.context = context;
        expenseEntryMapper = new ExpenseEntryMapper(context);
    }

    public boolean save(ExpenseEntry expenseEntry) {
        return expenseEntryMapper.insert(expenseEntry);
    }

    public ArrayList<ExpenseEntry> listAll() {
        return expenseEntryMapper.listAll();
    }

    public void deleteAll() {
        expenseEntryMapper.deleteAll();
    }
}
