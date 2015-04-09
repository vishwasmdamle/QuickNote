package com.example.vishwasdamle.quicknote.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by vishwasdamle on 07/04/15.
 */
public class AutoCompleteMapper extends DatabaseBuilder {


    public static final String SELECT_QUERY = "SELECT * FROM " + TABLE_NAME_AUTOCOMPLETE + " ORDER BY " + OCCURRENCE + " DESC";
    public static final String UPDATE_QUERY = "UPDATE " + TABLE_NAME_AUTOCOMPLETE + " SET " + OCCURRENCE + " = " + OCCURRENCE + "+1 WHERE " + WORD + " = ?";
    private static final String DELETE_QUERY = "DELETE FROM " + TABLE_NAME_AUTOCOMPLETE;

    public AutoCompleteMapper(Context context) {
        super(context);
    }

    public void update(String[] words) {
        ContentValues contentValues;
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        for(String word : words) {
            contentValues = new ContentValues();
            contentValues.put(WORD, word);
            contentValues.put(OCCURRENCE, 1);
            if(sqLiteDatabase.insertWithOnConflict(TABLE_NAME_AUTOCOMPLETE, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE) == -1) {
                sqLiteDatabase.execSQL(UPDATE_QUERY, new String[]{word});
            }
        }
        sqLiteDatabase.close();
    }

    public ArrayList<String> listAll() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(SELECT_QUERY, null);
        ArrayList<String> wordList = new ArrayList<>();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            wordList.add(cursor.getString(cursor.getColumnIndex(WORD)));
            cursor.moveToNext();
        }
        sqLiteDatabase.close();
        return wordList;
    }

    public void deleteAll() {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.execSQL(DELETE_QUERY);
        sqLiteDatabase.close();
    }
}
