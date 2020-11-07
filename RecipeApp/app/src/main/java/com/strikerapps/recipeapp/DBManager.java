package com.strikerapps.recipeapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {
    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public DBManager(Context c) {
        context = c;
    }

    public DBManager open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(String  name,String image, String category, String label, String price, String description) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.NAME, name);
        contentValue.put(DatabaseHelper.IMAGE, image);
        contentValue.put(DatabaseHelper.CATEGORY, category);
        contentValue.put(DatabaseHelper.LABEL, label);
        contentValue.put(DatabaseHelper.PRICE, price);
        contentValue.put(DatabaseHelper.DESCRIPTION, description);
        database.insert(DatabaseHelper.TABLE_NAME, null, contentValue);
    }

    public Cursor fetch() {
        String[] columns = new String[] { DatabaseHelper.ID, DatabaseHelper.NAME, DatabaseHelper.IMAGE,DatabaseHelper.CATEGORY,DatabaseHelper.LABEL,
                DatabaseHelper.PRICE,DatabaseHelper.DESCRIPTION};
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int update(long _id, String  name,String image, String category, String label, String price, String description) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.NAME, name);
        contentValues.put(DatabaseHelper.IMAGE, image);
        contentValues.put(DatabaseHelper.CATEGORY, category);
        contentValues.put(DatabaseHelper.LABEL, label);
        contentValues.put(DatabaseHelper.PRICE, price);
        contentValues.put(DatabaseHelper.DESCRIPTION, description);int i = database.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper.ID + " = " + _id, null);
        return i;
    }

    public void delete(long _id) {
        database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper.ID + "=" + _id, null);
    }
}
