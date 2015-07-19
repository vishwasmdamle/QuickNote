package com.example.vishwasdamle.quicknote.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.vishwasdamle.quicknote.model.ExpenseEntry;
import com.example.vishwasdamle.quicknote.model.ExpenseType;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.NoSuchElementException;

public class ExpenseEntryMapper extends DatabaseBuilder {

  private static final String DELETE_ALL_QUERY = "DELETE FROM " + TABLE_NAME_EXPENSE;
  private static final String SELECT_ALL_QUERY = "SELECT * FROM " + TABLE_NAME_EXPENSE
      + " ORDER BY " + UID + " DESC";
  private static final String DELETE_QUERY = "DELETE FROM " + TABLE_NAME_EXPENSE
      + " WHERE " + UID + "=";
  private static final String SELECT_QUERY = "SELECT * FROM " + TABLE_NAME_EXPENSE
      + " WHERE " + UID + "=";

  public ExpenseEntryMapper(Context context) {
    super(context);
  }

  public boolean saveOrUpdate(ExpenseEntry expenseEntry) {
    SQLiteDatabase sqLiteDatabase = getWritableDatabase();
    ContentValues contentValues = generateContentValues(expenseEntry);
    if (expenseEntry.getUid() != 0) {
      return sqLiteDatabase.update(TABLE_NAME_EXPENSE,
          contentValues, UID + "=" + expenseEntry.getUid(), null) != -1;
    } else {
      return sqLiteDatabase.insert(TABLE_NAME_EXPENSE, null, contentValues) != -1;
    }
  }

  public ArrayList<ExpenseEntry> listAll() {
    Cursor cursor = executeSelectionQuery(SELECT_ALL_QUERY);
    return getExpenseEntries(cursor);
  }

  public void deleteAll() {
    executeMutationQuery(DELETE_ALL_QUERY);
  }

  public void delete(long uid) {
    String sql = DELETE_QUERY + uid;
    executeMutationQuery(sql);
  }

  public ExpenseEntry get(long uid) {
    Cursor cursor = executeSelectionQuery(SELECT_QUERY + uid);
    cursor.moveToFirst();
    if (!cursor.isAfterLast()) {
      return buildExpenseEntry(cursor);
    } else {
      throw new NoSuchElementException();
    }
  }

  private void executeMutationQuery(String sql) {
    SQLiteDatabase sqLiteDatabase = getWritableDatabase();
    sqLiteDatabase.execSQL(sql);
    sqLiteDatabase.close();
  }

  private Cursor executeSelectionQuery(String query) {
    SQLiteDatabase sqLiteDatabase = getReadableDatabase();
    return sqLiteDatabase.rawQuery(query, null);
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

  private ArrayList<ExpenseEntry> getExpenseEntries(Cursor cursor) {
    ArrayList<ExpenseEntry> expenseEntryArrayList = new ArrayList<>();
    cursor.moveToFirst();
    while (!cursor.isAfterLast()) {
      expenseEntryArrayList.add(
          buildExpenseEntry(cursor)
      );
      cursor.moveToNext();
    }
    return expenseEntryArrayList;
  }

  private ExpenseEntry buildExpenseEntry(Cursor cursor) {
    return new ExpenseEntry(
        cursor.getLong(cursor.getColumnIndex(UID)),
        DateTime.parse(cursor.getString(cursor.getColumnIndex(TIMESTAMP))),
        ExpenseType.valueOf(
            cursor.getString(cursor.getColumnIndex(EXPENSE_TYPE))
        ),
        cursor.getDouble(cursor.getColumnIndex(AMOUNT)),
        cursor.getString(cursor.getColumnIndex(DESCRIPTION))
    );
  }
}
