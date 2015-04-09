package com.example.vishwasdamle.quicknote.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.vishwasdamle.quicknote.model.ExpenseEntry;
import com.example.vishwasdamle.quicknote.model.ExpenseType;

import org.joda.time.DateTime;

import java.util.ArrayList;

/**
 * Created by vishwasdamle on 03/04/15.
 */
public class ExpenseEntryMapper extends DatabaseBuilder {

    private static final String SELECT_QUERY = "SELECT * FROM " + TABLE_NAME_EXPENSE + " ORDER BY " + UID + " DESC";
    private static final String DELETE_QUERY = "DELETE FROM " + TABLE_NAME_EXPENSE;

    public ExpenseEntryMapper(Context context) {
        super(context);
    }

    public boolean insert(ExpenseEntry expenseEntry) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        ContentValues contentValues = generateContentValues(expenseEntry);

        return sqLiteDatabase.insert(TABLE_NAME_EXPENSE, null, contentValues) != -1;

    }

    private ContentValues generateContentValues(ExpenseEntry expenseEntry) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TIMESTAMP, String.valueOf(expenseEntry.getTimeStamp()));
        contentValues.put(EXPENSE_TYPE, String.valueOf(expenseEntry.getExpenseType()));
        contentValues.put(AMOUNT, expenseEntry.getAmount());
        contentValues.put(DESCRIPTION, expenseEntry.getDescription());
        contentValues.put(ACCOUNT, expenseEntry.getAccount());
        return contentValues;
    }

    public ArrayList<ExpenseEntry> listAll() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(SELECT_QUERY, null);
        ArrayList<ExpenseEntry> expenseEntryArrayList = new ArrayList<>();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            expenseEntryArrayList.add(
                    new ExpenseEntry(
                            cursor.getLong(cursor.getColumnIndex(UID)),
                            DateTime.parse(cursor.getString(cursor.getColumnIndex(TIMESTAMP))),
                            ExpenseType.valueOf(
                                    cursor.getString(cursor.getColumnIndex(EXPENSE_TYPE))
                            ),
                            cursor.getDouble(cursor.getColumnIndex(AMOUNT)),
                            cursor.getString(cursor.getColumnIndex(DESCRIPTION))
                    )
            );
            cursor.moveToNext();
        }
        return expenseEntryArrayList;
    }

    public void deleteAll() {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.execSQL(DELETE_QUERY);
        sqLiteDatabase.close();
    }
}
