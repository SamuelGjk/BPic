package com.happy.samuelalva.bcykari.ui.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.support.v7.app.AlertDialog;
import android.webkit.WebView;

import com.happy.samuelalva.bcykari.R;

/**
 * Created by Samuel.Alva on 2015/5/16.
 */
public class SettingsFragment extends PreferenceFragment {
    private static final String VERSION = "version";
    private static final String LICENSE = "license";

    private Preference mVersion;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

        mVersion = findPreference(VERSION);

        String version = "Unknown";
        try {
            version = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName;
        } catch (Exception e) {
        }
        mVersion.setSummary(version);
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        switch (preference.getKey()) {
            case LICENSE:
                showAboutDialog();
                return true;
            default:
                return super.onPreferenceTreeClick(preferenceScreen, preference);
        }
    }

    private void showAboutDialog() {
        WebView v = new WebView(getActivity());
        v.loadUrl("file:///android_asset/licenses.html");

        new AlertDialog.Builder(getActivity()).setView(v).setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // Noting to do
            }
        }).show();
    }
}
