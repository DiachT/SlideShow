package com.diacht.slideshow.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.diacht.slideshow.R;
import com.diacht.slideshow.system.Utils;
import com.nononsenseapps.filepicker.FilePickerActivity;

/**
 * SettingsFragment
 * @author Tetiana Diachuk (diacht@gmail.com)
 */
public class SettingsFragment extends PreferenceFragment {
    private static final int FILE_CODE = 24352;

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_settings);
        findPreference(getString(R.string.select_folder)).setSummary(
                getPreferenceScreen().getSharedPreferences().
                        getString(getString(R.string.select_folder), null));
        findPreference(getString(R.string.time_start)).setSummary(
                getPreferenceScreen().getSharedPreferences().
                        getString(getString(R.string.time_start), null));
        findPreference(getString(R.string.time_stop)).setSummary(
                getPreferenceScreen().getSharedPreferences().
                        getString(getString(R.string.time_stop), null));
        findPreference(getString(R.string.select_folder)).
                setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        selectFolder();
                        return true;
                    }
                });

        findPreference(getString(R.string.time_start)).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Utils.setTimeStart(getActivity(), (String) newValue);
                return false;
            }
        });
    }

        private void selectFolder() {
        Intent i = new Intent(getActivity(), FilePickerActivity.class);
        // This works if you defined the intent filter
        // Intent i = new Intent(Intent.ACTION_GET_CONTENT);

        // Set these depending on your use case. These are the defaults.
        i.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false);
        i.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, false);
        i.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_DIR);

        // Configure initial directory by specifying a String.
        // You could specify a String like "/storage/emulated/0/", but that can
        // dangerous. Always use Android's API calls to get paths to the SD-card or
        // internal memory.
        i.putExtra(FilePickerActivity.EXTRA_START_PATH,
                getPreferenceScreen().getSharedPreferences().
                        getString(getString(R.string.select_folder),
                                Environment.getExternalStorageDirectory().getPath()));
        startActivityForResult(i, FILE_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FILE_CODE && resultCode == Activity.RESULT_OK) {
            getPreferenceScreen().getSharedPreferences().edit().putString(
                    getString(R.string.select_folder), data.getData().getPath()).apply();
            findPreference(getString(R.string.select_folder)).setSummary(data.getData().getPath());
        }
    }

    @Override
    public void onPause() {
        Utils.startSlideShowEvents(getActivity());
        super.onPause();
    }
}

