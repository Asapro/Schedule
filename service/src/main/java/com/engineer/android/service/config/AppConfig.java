package com.engineer.android.service.config;

import android.content.Context;

import com.engineer.android.library.handler.SharedPreferenceHandler;

/**
 *
 * Created by L.J on 2016/6/2.
 */
public class AppConfig {
    public static final String APP_CONFIG_NAME = "app_config";
    public static final String KEY_IS_FIRST_LAUNCH = "is_first_launch";
    private static AppConfig instance;

    public static void initialize(Context context){
        instance = new AppConfig(context.getApplicationContext());
    }
    public static AppConfig getInstance(){
        return instance;
    }

    private SharedPreferenceHandler sharedPreferenceHandler;
    private AppConfig(Context context){
        this.sharedPreferenceHandler = SharedPreferenceHandler.newInstance(context,APP_CONFIG_NAME);
    }

    public boolean isFirstLaunch(){
        return this.sharedPreferenceHandler.getBoolean(KEY_IS_FIRST_LAUNCH,true);
    }

    public void setFirstLaunch(boolean value){
        this.sharedPreferenceHandler.putBoolean(KEY_IS_FIRST_LAUNCH,value);
    }
}
