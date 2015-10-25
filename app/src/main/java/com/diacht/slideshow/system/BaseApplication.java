package com.diacht.slideshow.system;

import android.app.Application;

/**
 * Application
 * @author Tetiana Diachuk (diacht@gmail.com)
 */

public class BaseApplication extends Application{
    private BaseSettings mSettings;

    @Override
    public void onCreate() {
        super.onCreate();
        mSettings = new BaseSettings(this);
    }

    public BaseSettings getSettings() {
        return mSettings;
    }
}
