package com.engineer.android.schedule.presenter;

import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import com.engineer.android.schedule.interaction.MainAction;

/**
 *
 * Created by L.J on 2016/6/3.
 */
public class MainPresenter extends BasePresenter implements ActivityCompat.OnRequestPermissionsResultCallback {
    private MainAction action;

    public MainPresenter(MainAction action) {
        this.action = action;
    }

    public void onNavCameraClick(){
        this.action.openCamera();
    }

    public void onNavGalleryClick(){
        this.action.openGallery();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

    }
}
