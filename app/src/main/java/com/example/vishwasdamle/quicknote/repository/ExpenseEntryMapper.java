package com.example.vishwasdamle.quicknote.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.vishwasdamle.quicknote.model.ExpenseEntry;
import com.example.vishwasdamle.quicknote.model.ExpenseType;

import org.joda.time.DateTime;

import java.util.ArrayList;

/**
 * Created by vishwasdamle on 03/04/15.
 */
public class ExpenseEntryMapper extends SQLiteOpenHelper {
    private static final String TABLE_NAME = "ExpenseTable";
    private static final String TIMESTAMP = "timestamp";
    private static final String EXPENSE_TYPE = "type";
    private static final String AMOUNT = "amount";
    private static final String DESCRIPTION = "description";
    private static final String DB_NAME = "QuickNote";

    public ExpenseEntryMapper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" + TIMESTAMP + ", " + EXPENSE_TYPE + ", " + AMOUNT + ", " + DESCRIPTION + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
    }

    public boolean insert(ExpenseEntry expenseEntry) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        ContentValues contentValues = generateContentValues(expenseEntry);

        return sqLiteDatabase.insert(TABLE_NAME, null, contentValues) != -1;

    }

    private ContentValues generateContentValues(ExpenseEntry expenseEntry) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TIMESTAMP, String.valueOf(expenseEntry.getTimeStamp()));
        contentValues.put(EXPENSE_TYPE, String.valueOf(expenseEntry.getExpenseType()));
        contentValues.put(AMOUNT, expenseEntry.getAmount());
        contentValues.put(DESCRIPTION, expenseEntry.getDescription());
        return contentValues;
    }

    public ArrayList<ExpenseEntry> listAll() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        ArrayList<ExpenseEntry> expenseEntryArrayList = new ArrayList<>();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            expenseEntryArrayList.add(
                    new ExpenseEntry(
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
}
