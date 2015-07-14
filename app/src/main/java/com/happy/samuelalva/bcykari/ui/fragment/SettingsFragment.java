package com.happy.samuelalva.bcykari.ui.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.webkit.WebView;

import com.happy.samuelalva.bcykari.R;
import com.happy.samuelalva.bcykari.support.Utility;

import java.io.File;
import java.text.DecimalFormat;

/**
 * Created by Samuel.Alva on 2015/5/16.
 */
public class SettingsFragment extends PreferenceFragment {
    private static final String VERSION = "version";
    private static final String LICENSE = "license";
    private static final String CLEAN_CACHE = "clean_cache";

    private Preference mCleanCache;

    private String cacheSummaryStr;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

        cacheSummaryStr = getResources().getString(R.string.cache_size);

        mCleanCache = findPreference(CLEAN_CACHE);
        Preference mVersion = findPreference(VERSION);

        mCleanCache.setSummary(String.format(cacheSummaryStr, getCacheFolderSize()));

        String version = "Unknown";
        try {
            version = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        mVersion.setSummary(version);
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, @NonNull Preference preference) {
        switch (preference.getKey()) {
            case LICENSE:
                showAboutDialog();
                return true;
            case CLEAN_CACHE:
                new DeleteTask().execute();
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
            }
        }).show();
    }

    private String getCacheFolderSize() {
        float size = 0;
        File[] files = Utility.getCacheFiles();
        if (files.length != 0) {
            for (File file : files) {
                size += file.length();
            }
        }
        return new DecimalFormat("0.00").format(size / (1024 * 1024));
    }

    private class DeleteTask extends AsyncTask<Void, Void, Boolean> {
        ProgressDialog dialog = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage(getResources().getString(R.string.cleaning_cache_tip));
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return Utility.cleanCache();
        }

        @Override
        protected void onPostExecute(Boolean b) {
            super.onPostExecute(b);
            dialog.dismiss();
            mCleanCache.setSummary(String.format(cacheSummaryStr, "0.00"));
        }
    }
}
