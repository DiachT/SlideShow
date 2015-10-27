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

    public void startSlideShowEvents() {
        boolean enabled = PreferenceManager.getDefaultSharedPreferences(this).
                getBoolean(getString(R.string.start_power), true);
        int flag = (enabled ?
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED :
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED);
        ComponentName component = new ComponentName(this, PowerReceiver.class);
        getPackageManager()
                .setComponentEnabledSetting(component, flag,
                        PackageManager.DONT_KILL_APP);

        enabled = PreferenceManager.getDefaultSharedPreferences(this).
                getBoolean(getString(R.string.start_reboot), true);
        flag = (enabled ?
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED :
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED);
        component = new ComponentName(this, RebootReceiver.class);
        getPackageManager()
                .setComponentEnabledSetting(component, flag,
                        PackageManager.DONT_KILL_APP);
    }

    public BaseSettings getSettings() {
        return mSettings;
    }
}
