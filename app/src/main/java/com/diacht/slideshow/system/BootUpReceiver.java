package com.diacht.slideshow.system;

        import android.content.BroadcastReceiver;
        import android.content.Context;
        import android.content.Intent;

        import com.diacht.slideshow.ui.MainActivity;

/**
 * BroadcastReceiver - start application after reboot
 * @author Tetiana Diachuk (diacht@gmail.com)
 */
public class BootUpReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }}
