package com.example.retofit2.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {
    private static SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;


    public SharedPrefManager(Context context){
        sharedPreferences = context.getSharedPreferences("appPrefs", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    //Save token into SharedPreferences
    public void saveToken(String token){
        editor.putString("JWT_TOKEN", token);
        editor.apply();
    }

    //Save phone number into SharedPreferences
    public void savePhoneNumber(String phoneNumber){
        editor.putString("PHONE_NUMBER", phoneNumber);
        editor.apply();
    }

    //Save Id User into SharedPreferences
    public void saveUserId(Long userId){
        editor.putLong("USER_ID", userId);
        editor.apply();
    }

    //Get token from SharedPreferences
    public String getToken(){
        return sharedPreferences.getString("JWT_TOKEN", null);
    }


    //Get phone number from SharedPreferences
    public String getPhoneNumber(){
        return sharedPreferences.getString("PHONE_NUMBER", null);
    }
    public static Long getUserId(){
        return sharedPreferences.getLong("USER_ID", 0);
    }

    public boolean hasToken(){
        return sharedPreferences.contains("JWT_TOKEN");
    }

    //Xoa tat ca dataa trong SharedPreferences
    public void clearSharedPreferences(){
        editor.clear();
        editor.apply();
    }


    //Xoa token cu the
    public void removeToken(){
        editor.remove("JWT_TOKEN");
        editor.apply();
    }
}
