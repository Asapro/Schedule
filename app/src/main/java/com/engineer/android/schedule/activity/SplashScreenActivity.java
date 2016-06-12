package com.engineer.android.schedule.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import com.engineer.android.schedule.R;
import com.engineer.android.schedule.interaction.SplashScreenAction;
import com.engineer.android.schedule.presenter.SplashScreenPresenter;
import com.engineer.android.service.ServiceProvider;

public class SplashScreenActivity extends BaseActivity<SplashScreenPresenter> implements SplashScreenAction{


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
    }

    @Override
    public void initializeService() {
        ServiceProvider.initialize(this);
    }

    @Override
    public void startMainActivity() {
        MainActivity.start(this);
    }

    @Override
    public void startGuideActivity() {
        GuideActivity.start(this);
    }

    @Override
    public AppCompatActivity provideContext() {
        return this;
    }

    @Override
    public void finishActivity() {
        ActivityCompat.finishAfterTransition(this);
    }

    @Override
    protected SplashScreenPresenter newPresenter() {
        return new SplashScreenPresenter(this);
    }
}
