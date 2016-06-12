package com.engineer.android.schedule.interaction;

import android.support.v7.app.AppCompatActivity;

/**
 *
 * Created by L.J on 2016/6/3.
 */
public interface BaseAction {
    AppCompatActivity provideContext();
    void finishActivity();
}
