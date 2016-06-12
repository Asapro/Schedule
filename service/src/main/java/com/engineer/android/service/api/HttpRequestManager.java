package com.engineer.android.service.api;

import android.content.Context;

import com.engineer.android.library.handler.AndroidHttpRequestHandler;

/**
 *
 * Created by L.J on 2016/6/10.
 */
public class HttpRequestManager {
    private static HttpRequestManager instance;

    public static void initialize(Context context){
        instance = new HttpRequestManager(context.getApplicationContext());
    }

    public static HttpRequestManager getInstance(){
        return instance;
    }

    private Context context;
    private AndroidHttpRequestHandler requestHandler;
    private HttpRequestManager(Context context) {
        this.context = context;
        this.requestHandler = AndroidHttpRequestHandler.getInstance();
    }
}
