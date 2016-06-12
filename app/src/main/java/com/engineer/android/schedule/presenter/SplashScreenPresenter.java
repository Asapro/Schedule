package com.engineer.android.schedule.presenter;

import com.engineer.android.schedule.interaction.SplashScreenAction;
import com.engineer.android.service.ServiceProvider;
import com.engineer.android.service.config.AppConfig;

/**
 *
 * Created by L.J on 2016/5/31.
 */
public class SplashScreenPresenter extends BasePresenter {
    private SplashScreenAction action;

    private Runnable delayAction = new Runnable() {
        @Override
        public void run() {
            timeUp();
        }
    };

    public SplashScreenPresenter(SplashScreenAction action){
        this.action = action;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.action.initializeService();
        delayStart();
    }

    private void timeUp(){
        if(ServiceProvider.getAppConfig().isFirstLaunch()){
            this.action.startGuideActivity();
        }else{
            this.action.startMainActivity();
        }
        this.action.finishActivity();
    }

    private void delayStart() {
        ServiceProvider.getTimerActionManger().requestDelayAction(delayAction,3000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ServiceProvider.getTimerActionManger().removeAction(delayAction);
    }
}
