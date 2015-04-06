package com.example.vishwasdamle.quicknote.model;

import android.content.Context;

import com.example.vishwasdamle.quicknote.R;

import java.util.ArrayList;

/**
 * Created by vishwasdamle on 03/04/15.
 */
public enum ExpenseType {
    CREDIT(R.string.Credit), DEBIT(R.string.Debit), REVERT(R.string.Revert);

    int stringId;

    ExpenseType(int id) {
        this.stringId = id;
    }

    public int getStringId() {
        return stringId;
    }

    static public ArrayList<String> getStringValues(Context context) {
        ArrayList<String> stringValues = new ArrayList<>();
        for (ExpenseType value : ExpenseType.values()) {
            stringValues.add(context.getResources().getString(value.stringId));
        }
        return stringValues;
    }
}