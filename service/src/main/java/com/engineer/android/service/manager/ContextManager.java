package com.engineer.android.service.manager;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

/**
 * Context Manager provide global context.
 * Created by L.J on 2016/5/31.
 */
public class ContextManager {
    private static ContextManager ourInstance;

    public static void initialize(Context context){
        ourInstance = new ContextManager(context.getApplicationContext());
    }

    public static ContextManager getInstance() {
        if(ourInstance == null) {
            throw new NullPointerException("Call initialize first before calling this method.");
        }
        return ourInstance;
    }

    private Context context;

    private ContextManager(Context context) {
        this.context = context;
    }

    public Context getContext(){
        return context;
    }

    public InputStream openAsset(String filename) throws IOException {
        return this.context.getAssets().open(filename);
    }

    public InputStream openRaw(int id){
        return this.context.getResources().openRawResource(id);
    }
}
