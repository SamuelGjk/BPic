/*
 * Copyright 2015 SamuelGjk <samuel.alva@outlook.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package moe.yukinoneko.bpic.ui.fragment;

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
import android.text.format.Formatter;
import android.webkit.WebView;

import java.io.File;

import moe.yukinoneko.bpic.BPicApplication;
import moe.yukinoneko.bpic.R;

/**
 * Created by Samuel.Alva on 2015/5/16.
 */
public class SettingsFragment extends PreferenceFragment {
    private static final String VERSION = "version";
    private static final String LICENSE = "license";
    private static final String CLEAN_CACHE = "clean_cache";

    private Preference mCleanCache;

    private boolean clean;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

        mCleanCache = findPreference(CLEAN_CACHE);
        Preference mVersion = findPreference(VERSION);

        setCacheSizeSummary(getCacheFolderSize());

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
                if (clean)
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
        long size = 0;
        File[] files = getCacheFiles();
        if (files.length != 0) {
            clean = true;
            for (File file : files) {
                size += file.length();
            }
        }
        return Formatter.formatFileSize(getActivity(), size);
    }

    private void setCacheSizeSummary(String size) {
        mCleanCache.setSummary(getString(R.string.cache_size) + size);
    }

    private File[] getCacheFiles() {
        File mCacheDir = BPicApplication.getImageCacheDir();
        return mCacheDir.listFiles();
    }

    private class DeleteTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog dialog = null;

        private void cleanCache() {
            File[] files = getCacheFiles();
            if (files.length != 0) {
                for (File file : files) {
                    file.delete();
                }
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage(getString(R.string.cleaning_cache_tip));
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            cleanCache();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            clean = false;
            dialog.dismiss();
            setCacheSizeSummary("0.00B");
        }
    }
}
