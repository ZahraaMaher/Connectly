package com.example.connectly;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.connectly.jobprovider.Job;

import static android.content.Context.MODE_PRIVATE;

public class SharedPrefManager {

    public static Job g_Job = new Job();

    private static SharedPreferences sharedPref;
    private static SharedPrefManager sharedPrefManager;

    private static final String package_name = "com.zainulmihor.gashtbook";

    private static final String keyPassword = package_name + "password";
    private static final String keyEmail = package_name + "email";
    private static final String keyLocation = package_name + "location";

    private SharedPrefManager(Context context){
        sharedPref = context.getSharedPreferences(package_name, MODE_PRIVATE);

    }

    public static synchronized SharedPrefManager getSharedPrefManager(Context context){

        if(sharedPrefManager == null){
            sharedPrefManager = new SharedPrefManager(context);
        }

        return sharedPrefManager;
    }



    static public void setEmail(String value){
        sharedPref.edit().putString(keyEmail, value).apply();
    }

    static public String getEmail(){
        return sharedPref.getString(keyEmail, "");
    }

    static public void setPassword(String value){
        sharedPref.edit().putString(keyPassword, value).apply();
    }

    static public String getPassword(){
        return sharedPref.getString(keyPassword, "");
    }

    static public void setLocation(String value){
        sharedPref.edit().putString(keyLocation, value).apply();
    }

    static public String getLocation(){
        return sharedPref.getString(keyLocation, "");
    }

    static public void removeAllSharedPrefValue(){
        sharedPref.edit().remove(keyEmail).apply();
        sharedPref.edit().remove(keyPassword).apply();
        sharedPref.edit().remove(keyLocation).apply();
    }

}
