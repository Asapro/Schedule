package com.engineer.android.schedule.presenter;

import com.engineer.android.schedule.interaction.GuideAction;
import com.engineer.android.service.ServiceProvider;
import com.engineer.android.service.config.AppConfig;

/**
 *
 * Created by L.J on 2016/6/3.
 */
public class GuidePresenter extends BasePresenter {
    private GuideAction action;

    public GuidePresenter(GuideAction action){
        this.action = action;
    }

    public void onStartNowButtonClick(){
        ServiceProvider.getAppConfig().setFirstLaunch(false);
        this.action.startMainActivity();
        this.action.finishActivity();
    }
}
