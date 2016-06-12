package com.engineer.android.schedule.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import com.engineer.android.schedule.presenter.BasePresenter;

/**
 *
 * Created by L.J on 2016/5/31.
 */
public abstract class BaseActivity<T extends BasePresenter>  extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback{
    protected String TAG;
    protected T presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.presenter = newPresenter();
        if(this.presenter != null) {
            this.presenter.onCreate();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(this.presenter != null){
            this.presenter.onStart();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(this.presenter != null){
            this.presenter.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(this.presenter != null){
            this.presenter.onPause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(this.presenter != null) {
            this.presenter.onStop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(this.presenter != null){
            this.presenter.onDestroy();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(this.presenter != null){
            this.presenter.onBackPressed();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(this.presenter != null){
            this.presenter.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    protected T getPresenter(){
        return this.presenter;
    }

    protected abstract T newPresenter();

}
