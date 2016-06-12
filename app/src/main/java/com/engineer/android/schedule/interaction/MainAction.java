package com.engineer.android.schedule.interaction;


/**
 *
 * Created by L.J on 2016/6/4.
 */
public interface MainAction extends BaseAction {
    void startSettingActivity();
    void startHistoryActivity();
    void startEditActivity();
    void showCalendarDialog();
    void openCamera();
    void openGallery();
    void openTools();
    void openHistory();
}
