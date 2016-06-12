package com.engineer.android.service.manager;

import android.os.Handler;
import android.os.Message;

/**
 *
 * Created by L.J on 2016/6/2.
 */
public class TimerActionManager {
    private static TimerActionManager instance = new TimerActionManager();

    public static TimerActionManager getInstance(){
        return instance;
    }

    private TimerActionHandler handler;
    private TimerActionManager(){
        this.handler = new TimerActionHandler(this);
    }

    public void requestDelayAction(Runnable action,long delayTimeout){
        this.handler.postDelayed(action,delayTimeout);
    }

    public void removeAction(Runnable action){
        this.handler.removeCallbacks(action);
    }

    public interface OnTimeUpListener{
        void onTimeUp(Message message);
    }

    private static class TimerActionHandler extends Handler {
        private TimerActionManager manager;

        public TimerActionHandler(TimerActionManager manager){
            super();
            this.manager = manager;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

        }
    }
}
