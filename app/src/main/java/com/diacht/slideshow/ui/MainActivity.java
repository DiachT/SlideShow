package com.diacht.slideshow.ui;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.diacht.slideshow.R;
import com.diacht.slideshow.system.Utils;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FilenameFilter;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * SlideShowActivity
 * @author Tetiana Diachuk (diacht@gmail.com)
 */
public class MainActivity extends AppCompatActivity {
    private static final int SECOND = 1000;
    @InjectView(R.id.image_first)
    protected ImageView mImageFirst;
    @InjectView(R.id.image_second)
    protected ImageView mImageSecond;
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);


        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        findViewById(R.id.settings_button).setOnTouchListener(mDelayHideTouchListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        beginSlideShow();
        Utils.startSlideShowEvents(this);
        IntentFilter filter = new IntentFilter(Intent.ACTION_POWER_DISCONNECTED);
        filter.addAction(Utils.ACTION_STOP);
        registerReceiver(stopReceiver, filter);
        Utils.setTimeStop(this, PreferenceManager.getDefaultSharedPreferences(MainActivity.this).
                getString(getString(R.string.time_stop), ""));
    }

    @Override
    protected void onPause() {
        unregisterReceiver(stopReceiver);
        Utils.setTimeStart(this, PreferenceManager.getDefaultSharedPreferences(MainActivity.this).
                getString(getString(R.string.time_start), ""));
        super.onPause();
    }

    private BroadcastReceiver stopReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(Utils.ACTION_STOP)){
                finish();
            }else
            if (PreferenceManager.getDefaultSharedPreferences(MainActivity.this).
                    getBoolean(getString(R.string.start_power), true)) {
                finish();
            }
        }
    };

    private void beginSlideShow() {
        if (PreferenceManager.getDefaultSharedPreferences(MainActivity.this).
                getString(getString(R.string.select_folder), null) != null) {
            File parentDir = new File(PreferenceManager.getDefaultSharedPreferences(MainActivity.this).
                    getString(getString(R.string.select_folder), ""));
            if (parentDir.isDirectory()) {
                FilenameFilter imageFilter = new FilenameFilter() {
                    public boolean accept(File file, String name) {
                        if (name.toLowerCase().endsWith(".jpg")) {
                            // filters files whose extension is .jpg
                            return true;
                        } else {
                            return false;
                        }
                    }
                };
                File[] files = parentDir.listFiles(imageFilter);
                if(files != null && files.length > 0) {
                    mImageFirst.setVisibility(View.VISIBLE);
                    mImageSecond.setVisibility(View.VISIBLE);
                    showImage(loadImageIntoView(mImageFirst, files, 0), files, true);
                }else{
                    goneImages();
                }
            }else {
                goneImages();
            }
        }else{
            goneImages();
        }
    }

    private void goneImages(){
        mImageFirst.setVisibility(View.GONE);
        mImageSecond.setVisibility(View.GONE);
    }

    private int loadImageIntoView(ImageView view, File[] files, int i){
        Picasso.with(this).load(files[i]).memoryPolicy(MemoryPolicy.NO_CACHE).
                fit().centerInside().into(view);
        if((++ i) >= files.length) i = 0;
        return i;
    }

    private void showImage(int i, final File[] files, final boolean isFirstVisible){
        final int temp = loadImageIntoView(isFirstVisible ? mImageSecond : mImageFirst, files, i);
        mImageFirst.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mImageFirst != null) {
                    mImageFirst.setVisibility(isFirstVisible ? View.INVISIBLE : View.VISIBLE);
                    showImage(temp, files, !isFirstVisible);
                }
            }
        }, SECOND * Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(this).
                getString(getString(R.string.update_interval), "5")));
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    @Override
    protected void onDestroy() {
        ButterKnife.reset(this);
        super.onDestroy();
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    @OnClick(R.id.settings_button)
    public void onSettings() {
        startActivity(new Intent(MainActivity.this, SettingsActivity.class));
    }
}
