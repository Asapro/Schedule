package com.engineer.android.service.manager;

import android.content.Context;

/**
 *
 * Created by L.J on 2016/6/9.
 */
public class MediaManger {
    private static MediaManger instance;

    public static void initialize(Context context){
        instance = new MediaManger(context.getApplicationContext());
    }

    public static MediaManger getInstance(){
        return instance;
    }

    private Context context;
    private MediaManger(Context context){
        this.context = context;
    }


}
