package com.engineer.android.service.manager;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.PermissionChecker;
import android.support.v4.net.ConnectivityManagerCompat;

/**
 *
 * Created by L.J on 2016/6/4.
 */
public class NetworkManager {
    private static NetworkManager instance;

    public static void initialize(Context context){
        instance = new NetworkManager(context.getApplicationContext());
    }

    public static NetworkManager geInstance() {
        return instance;
    }
    private NetworkInfo networkInfo;
    private NetworkManager(Context context){
        if(PermissionChecker.checkSelfPermission(context, Manifest.permission.ACCESS_NETWORK_STATE) == PermissionChecker.PERMISSION_GRANTED) {
            NetworkBroadcastReceiver networkBroadcastReceiver = new NetworkBroadcastReceiver();
            context.registerReceiver(networkBroadcastReceiver, networkBroadcastReceiver.getFilter());
        }
    }

    public NetworkInfo getNetworkInfo(){
        return this.networkInfo;
    }

    private class NetworkBroadcastReceiver extends BroadcastReceiver {
        private IntentFilter filter;
        public NetworkBroadcastReceiver(){
            this.filter = new IntentFilter();
            this.filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        }

        public IntentFilter getFilter(){
            return this.filter;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            networkInfo = ConnectivityManagerCompat.getNetworkInfoFromBroadcast(connectivityManager,intent);
        }
    }
}
