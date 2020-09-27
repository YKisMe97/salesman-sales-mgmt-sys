package com.example.priyanka.mapsnearbyplaces.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreferenceStorage {
    public static final String USERNAME = "USERNAME";
    public static final String USER_PASSWORD= "USER_PASSWORD";
    public static final String IS_NEW_INSTALLED = "IS_NEW_INSTALLED";
    public static final String IS_AUTO_SYNC_FINISHED= "IS_AUTO_SYNC_FINISHED";

    public static String getStringValue(Context context, String key){
        SharedPreferences appPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        if(key.equalsIgnoreCase(USERNAME)){
            return appPreferences.getString(key, "DEMO");
        }else if(key.equalsIgnoreCase(USER_PASSWORD)){
            return appPreferences.getString(key, "demo");
        }
        return appPreferences.getString(key, "");
    }

    public static boolean getBooleanValue(Context context, String key){
        SharedPreferences appPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        if(key.equalsIgnoreCase(IS_NEW_INSTALLED) || key.equalsIgnoreCase(IS_AUTO_SYNC_FINISHED)) 	return appPreferences.getBoolean(key, true);
        return appPreferences.getBoolean(key, false);
    }

    public static double geDoubleValue(Context context, String key){
        SharedPreferences appPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return appPreferences.getFloat(key, (float) 0.0);
    }


    public static long getLongValue(Context context, String key){
        SharedPreferences appPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return appPreferences.getLong(key, 0);
    }

    public static void setStringValue(Context context, String key, String value){
        SharedPreferences appPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = appPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void setBooleanValue(Context context, String key, boolean value){
        SharedPreferences appPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = appPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static void setIntValue(Context context, String key, int value){
        SharedPreferences appPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = appPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static void setDoubleValue(Context context, String key, double value){
        SharedPreferences appPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = appPreferences.edit();
        editor.putFloat(key, (float) value);
        editor.commit();
    }

    public static void setLongValue(Context context, String key, long value){
        SharedPreferences appPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = appPreferences.edit();
        editor.putLong(key, value);
        editor.commit();
    }
}
