package com.diacht.slideshow.system;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Settings
 * @author Tetiana Diachuk (diacht@gmail.com)
 */
public class BaseSettings {
    private final static String PREF_FOLDER = "PREF_FOLDER";
    private final static String PREF_TIME_START = "PREF_TIME_START";
    private final static String PREF_TIME_STOP = "PREF_TIME_STOP";
    private final static String PREF_INTERVAL = "PREF_INTERVAL";
    private final static String PREF_CHARGER = "PREF_CHARGER";
    private final static String PREF_REBOOT = "PREF_REBOOT";
    private SharedPreferences mSettings;
    private String mFolder;
    private long mTimeStart;
    private long mTimeStop;
    private int mInterval;
    private boolean mIsCharger;
    private boolean mIsReboot;

    public BaseSettings(Context context) {
        mSettings = PreferenceManager.getDefaultSharedPreferences(context);
        loadSettings();
    }

    protected void loadSettings() {
        mFolder = mSettings.getString(PREF_FOLDER, null);
        mTimeStart = mSettings.getLong(PREF_TIME_START, -1);
        mTimeStop = mSettings.getLong(PREF_TIME_STOP, -1);
        mInterval = mSettings.getInt(PREF_INTERVAL, 1);
        mIsCharger = mSettings.getBoolean(PREF_CHARGER, false);
        mIsReboot = mSettings.getBoolean(PREF_REBOOT, false);
    }

    public String getFolder() {
        return mFolder;
    }

    public void setFolder(String mFolder) {
        this.mFolder = mFolder;
        mSettings.edit().putString(PREF_FOLDER, mFolder).commit();
    }

    public long getTimeStart() {
        return mTimeStart;
    }

    public void setTimeStart(long mTimeStart) {
        this.mTimeStart = mTimeStart;
        mSettings.edit().putLong(PREF_TIME_START, mTimeStart).commit();
    }

    public long getTimeStop() {
        return mTimeStop;
    }

    public void setTimeStop(long mTimeStop) {
        this.mTimeStop = mTimeStop;
        mSettings.edit().putLong(PREF_TIME_STOP, mTimeStop).commit();
    }

    public int getInterval() {
        return mInterval;
    }

    public void setInterval(int mInterval) {
        this.mInterval = mInterval;
        mSettings.edit().putInt(PREF_INTERVAL, mInterval).commit();
    }

    public boolean isCharger() {
        return mIsCharger;
    }

    public void setIsCharger(boolean mIsCharger) {
        this.mIsCharger = mIsCharger;
        mSettings.edit().putBoolean(PREF_CHARGER, mIsCharger).commit();
    }

    public boolean isReboot() {
        return mIsReboot;
    }

    public void setIsReboot(boolean mIsReboot) {
        this.mIsReboot = mIsReboot;
        mSettings.edit().putBoolean(PREF_REBOOT, mIsReboot).commit();
    }
}
