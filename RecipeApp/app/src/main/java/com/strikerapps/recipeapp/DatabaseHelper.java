package com.strikerapps.recipeapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper{

    public static final String TABLE_NAME = "RECIPES";
    public static final String ID="id";
    public static final String NAME="name";
    public static final String IMAGE="image";
    public static final String CATEGORY="category";
    public static final String LABEL="label";
    public static final String PRICE="price";
    public static final String DESCRIPTION="description";
    static final String DB_NAME = "RECIPES_DATA.DB";
    static final int DB_VERSION = 1;

    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" + ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME + " TEXT NOT NULL, " + IMAGE + " TEXT,"+ CATEGORY + " TEXT,"+
            LABEL + " TEXT,"+ PRICE + " TEXT,"+ DESCRIPTION + " TEXT);";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
