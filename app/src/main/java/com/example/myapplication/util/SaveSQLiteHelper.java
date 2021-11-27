package com.example.myapplication.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SaveSQLiteHelper extends SQLiteOpenHelper {

    public SaveSQLiteHelper(Context context) {
        super(context, "MyDatabases.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
                "CREATE TABLE UserInfo(Account VARCHAR(20) PRIMARY KEY,Password VARCHAR(20))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

}
