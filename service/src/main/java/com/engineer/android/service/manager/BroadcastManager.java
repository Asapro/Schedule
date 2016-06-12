package com.engineer.android.service.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

/**
 *
 * Created by L.J on 2016/5/31.
 */
public class BroadcastManager {
    private static BroadcastManager ourInstance;

    public static void initialize(Context context){
        ourInstance = new BroadcastManager(context);
    }

    public static BroadcastManager getInstance() {
        return ourInstance;
    }

    private Context context;
    private LocalBroadcastManager manager;
    private BroadcastManager(Context context) {
        this.context = context;
        this.manager = LocalBroadcastManager.getInstance(context);
    }

    public void registerLocalBroadcastManger(BroadcastReceiver receiver, IntentFilter filter){
        this.manager.registerReceiver(receiver,filter);
    }

    public void unregisterLocalBroadcastManager(BroadcastReceiver receiver){
        this.manager.unregisterReceiver(receiver);
    }

    public void registerBroadcastReceiver(BroadcastReceiver receiver,IntentFilter filter){
        this.context.registerReceiver(receiver, filter);
    }

    public void unregisterBroadcastReceiver(BroadcastReceiver receiver){
        this.context.unregisterReceiver(receiver);
    }
}
