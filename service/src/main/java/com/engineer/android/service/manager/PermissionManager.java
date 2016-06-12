package com.engineer.android.service.manager;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;

/**
 * Request Calendar permission use {@link #REQUEST_CODE_CALENDAR}
 * <br>
 * Created by L.J on 2016/6/1.
 */
public class PermissionManager {
    public static final int REQUEST_CODE_CALENDAR = 0;
    public static final int REQUEST_CODE_CAMERA = 1;

    private static PermissionManager instance = new PermissionManager();

    public static PermissionManager getInstance(){
        return instance;
    }

    private PermissionManager() {

    }

    public boolean isCalendarPermissionGranted(Context context){
        return PermissionChecker.checkSelfPermission(context, Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED
                || PermissionChecker.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED;
    }

    public boolean isContactPermissionGranted(Context context) {
        return PermissionChecker.checkSelfPermission(context,Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
                || PermissionChecker.checkSelfPermission(context,Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_GRANTED;
    }

    public boolean isCameraPermissionGranted(Context context){
        return PermissionChecker.checkSelfPermission(context,Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }


    private boolean checkShouldShowRequestPermissionRationale(Activity activity,String permission,int requestCode){
        if(ActivityCompat.shouldShowRequestPermissionRationale(activity,permission)){
            return false;
        }else{
            ActivityCompat.requestPermissions(activity,new String[]{permission},requestCode);
            return true;
        }
    }

    /**
     * Request Calendar Permission.It will call {@link ActivityCompat#shouldShowRequestPermissionRationale} first to check if the user denied it before.
     * @param activity Activity which need Calendar permission
     * @return if the user denied before then return false else return true.
     */
    public boolean requestCalendarPermission(Activity activity,int requestCode){
        return checkShouldShowRequestPermissionRationale(activity,Manifest.permission.READ_CALENDAR,REQUEST_CODE_CALENDAR);
    }

    /**
     *
     * @param activity Activity which need Calendar permission
     * @return if the user denied before then return false else return true.
     */
    public boolean requestCameraPermission(Activity activity){
        return checkShouldShowRequestPermissionRationale(activity,Manifest.permission.CAMERA,REQUEST_CODE_CAMERA);
    }
}
