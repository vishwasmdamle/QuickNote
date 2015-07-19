package com.example.vishwasdamle.quicknote.repository;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.vishwasdamle.quicknote.model.Constants.DB_NAME;

public class DatabaseBuilder extends SQLiteOpenHelper {


  static final String TABLE_NAME_AUTOCOMPLETE = "AutoCompleteTable";
  static final String WORD = "word";
  static final String OCCURRENCE = "occurrence";

  static final String TABLE_NAME_EXPENSE = "ExpenseTable";
  static final String UID = "uid";
  static final String TIMESTAMP = "timestamp";
  static final String EXPENSE_TYPE = "type";
  static final String AMOUNT = "amount";
  static final String DESCRIPTION = "description";
  static final String ACCOUNT = "account";

  public DatabaseBuilder(Context context) {
    super(context, DB_NAME, null, 1);
  }

  @Override
  public void onCreate(SQLiteDatabase sqLiteDatabase) {
    sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME_AUTOCOMPLETE
        + "(" + WORD + " PRIMARY KEY, " + OCCURRENCE + " INTEGER)");
    sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME_EXPENSE
        + "(" + UID + " INTEGER PRIMARY KEY, " + TIMESTAMP + ", "
        + EXPENSE_TYPE + ", " + AMOUNT + ", " + DESCRIPTION + ", " + ACCOUNT + ")");

  }

  @Override
  public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

  }

}
