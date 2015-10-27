package com.diacht.slideshow.system;

import android.app.Application;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;

import com.diacht.slideshow.R;

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

    public void startAfterReboot() {
        boolean enabled = PreferenceManager.getDefaultSharedPreferences(this).
                getBoolean(getString(R.string.start_reboot), true);
        int flag = (enabled ?
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED :
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED);
        ComponentName component = new ComponentName(this, BootUpReceiver.class);
        getPackageManager()
                .setComponentEnabledSetting(component, flag,
                        PackageManager.DONT_KILL_APP);
    }

    public BaseSettings getSettings() {
        return mSettings;
    }
}
