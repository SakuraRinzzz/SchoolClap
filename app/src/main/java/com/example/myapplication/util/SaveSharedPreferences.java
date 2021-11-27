package com.example.myapplication.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

public class SaveSharedPreferences {

    public static boolean saveUserInfo(Context context, String account, String password)
    {
        SharedPreferences sp = context.getSharedPreferences("data",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("Account",account);
        editor.putString("Password",password);
        editor.commit();
        return true;
    }

    public static Map<String,String> getUserInfo(Context context)
    {
        SharedPreferences sp = context.getSharedPreferences("data",Context.MODE_PRIVATE);
        String account = sp.getString("Account",null);
        String password = sp.getString("Password",null);
        Map<String,String> map = new HashMap<>();
        map.put("Account",account);
        map.put("Password",password);
        return map;
    }

    public static boolean deleteUserInfo(Context context)
    {
        SharedPreferences sp = context.getSharedPreferences("data",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        return true;
    }

}
