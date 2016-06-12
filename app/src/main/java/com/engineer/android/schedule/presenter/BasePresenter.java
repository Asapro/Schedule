package com.engineer.android.schedule.presenter;

import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

/**
 *
 * Created by L.J on 2016/5/31.
 */
public abstract class BasePresenter implements ActivityCompat.OnRequestPermissionsResultCallback{
    protected final String TAG;
    public BasePresenter(){
        TAG = getClass().getSimpleName();
    }
    public void onCreate(){}
    public void onStart(){}
    public void onResume(){}
    public void onPause(){}
    public void onStop(){}
    public void onDestroy(){}
    public void onBackPressed(){}

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

    }
}
