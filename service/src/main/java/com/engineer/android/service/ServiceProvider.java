package com.engineer.android.service;

import android.content.Context;

import com.engineer.android.service.config.AppConfig;
import com.engineer.android.service.manager.BroadcastManager;
import com.engineer.android.service.manager.ContextManager;
import com.engineer.android.service.manager.MediaManger;
import com.engineer.android.service.manager.NetworkManager;
import com.engineer.android.service.manager.TimerActionManager;

/**
 *
 * Created by L.J on 2016/5/31.
 */
public class ServiceProvider {

    public static void initialize(Context context){
        ContextManager.initialize(context);
        BroadcastManager.initialize(context);
        NetworkManager.initialize(context);
        AppConfig.initialize(context);
        MediaManger.initialize(context);
    }

    public static ContextManager getContextManager(){
        return ContextManager.getInstance();
    }

    public static TimerActionManager getTimerActionManger(){
        return TimerActionManager.getInstance();
    }

    public static AppConfig getAppConfig(){
        return AppConfig.getInstance();
    }
}
