package com.diacht.slideshow.system;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;

import com.diacht.slideshow.R;
import com.diacht.slideshow.ui.MainActivity;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Utils
 * @author T.Diachuk (diacht@gmail.com)
 *
 */
public class Utils{

    public static final String ACTION_STOP = "com.diacht.slideshow.ACTION_STOP";

    public static void setTimeStart(Context context, String time){
        if(time.length() > 0) {
            AlarmManager alarmManager = (AlarmManager) context.
                    getSystemService(Context.ALARM_SERVICE);
            Intent i = new Intent(context, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pending = PendingIntent.getActivity(context, 0, i,
                    PendingIntent.FLAG_CANCEL_CURRENT);
            alarmManager.set(AlarmManager.RTC, getTimeMilliseconds(time), pending);
        }
    }

    public static void setTimeStop(Context context, String time){
        if(time.length() > 0) {
            AlarmManager alarmManager = (AlarmManager) context.
                    getSystemService(Context.ALARM_SERVICE);
            Intent i = new Intent(ACTION_STOP);
            PendingIntent pending = PendingIntent.getBroadcast(context, 1, i,
                    PendingIntent.FLAG_CANCEL_CURRENT);
            alarmManager.set(AlarmManager.RTC, getTimeMilliseconds(time), pending);
        }
    }

    private static long getTimeMilliseconds(String time){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getDefault());
        calendar.set(Calendar.HOUR_OF_DAY, TimePreference.getHour(time));
        calendar.set(Calendar.MINUTE, TimePreference.getMinute(time));
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date dateToday = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date dateTomorrow = calendar.getTime();
        Date dateCurrent = new Date();
        return dateCurrent.before(dateToday) ?
                dateToday.getTime() : dateTomorrow.getTime();
    }

    public static void startSlideShowEvents(Context context) {
        boolean enabled = PreferenceManager.getDefaultSharedPreferences(context).
                getBoolean(context.getString(R.string.start_power), true);
        int flag = (enabled ?
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED :
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED);
        ComponentName component = new ComponentName(context, PowerReceiver.class);
        context.getPackageManager()
                .setComponentEnabledSetting(component, flag,
                        PackageManager.DONT_KILL_APP);

        enabled = PreferenceManager.getDefaultSharedPreferences(context).
                getBoolean(context.getString(R.string.start_reboot), true);
        flag = (enabled ?
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED :
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED);
        component = new ComponentName(context, RebootReceiver.class);
        context.getPackageManager()
                .setComponentEnabledSetting(component, flag,
                        PackageManager.DONT_KILL_APP);
    }
}
