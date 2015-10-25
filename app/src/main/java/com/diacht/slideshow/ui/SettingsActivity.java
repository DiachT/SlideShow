package com.diacht.slideshow.ui;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.diacht.slideshow.R;

/**
 * SettingsActivity
 * @author Tetiana Diachuk (diacht@gmail.com)
 */
public class SettingsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        if (savedInstanceState == null) {
            startFragment(SettingsFragment.newInstance(), false);
        }
    }

    public void startFragment(Fragment fragment, boolean addToStack) {
               final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.container, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        if (addToStack) {
            ft.addToBackStack(fragment.getClass().getSimpleName());
        }
        ft.commit();
    }
}


