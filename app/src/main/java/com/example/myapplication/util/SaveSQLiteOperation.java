package com.example.myapplication.util;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SaveSQLiteOperation {

    public static void insert(Context context, String account,String password)
    {
        SaveSQLiteHelper helper = new SaveSQLiteHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Account",account);
        contentValues.put("Password",password);
        long id = db.insert("UserInfo",null,contentValues);
        db.close();
    }

    public static int delete(Context context, String account)
    {
        SaveSQLiteHelper helper = new SaveSQLiteHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        int number = db.delete("UserInfo","Account=?",new String[]{account});
        db.close();
        return number;
    }

    public static int update(Context context,String account,String password)
    {
        SaveSQLiteHelper helper = new SaveSQLiteHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Password",password);
        int number =
                db.update("UserInfo",values,"Account=?",new String[]{account});
        db.close();
        return number;
    }

    public static void find(Context context)
    {
        SaveSQLiteHelper helper = new SaveSQLiteHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM UserInfo",null);
        if (cursor.getCount()!=0)
        {
            while (cursor.moveToNext()){
                String account = cursor.getString(0);
                String password = cursor.getString(1);
            }
        }

    }
}
