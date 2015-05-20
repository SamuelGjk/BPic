package com.happy.samuelalva.bcykari.ui.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.happy.samuelalva.bcykari.support.Utility;

import java.io.File;

/**
 * Created by Samuel.Alva on 2015/5/17.
 */
public class EntryActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        File mCacheDir = new File(getExternalCacheDir().getPath() + "/image");
        if (!mCacheDir.exists()) {
            mCacheDir.mkdirs();
        }

        File mImageDir = new File(Environment.getExternalStorageDirectory().getPath() + "/BcyKari");
        if (!mImageDir.exists()) {
            mImageDir.mkdirs();
        }

        new DeleteTask().execute(mCacheDir);

        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    private class DeleteTask extends AsyncTask<File, Void, Integer> {
        @Override
        protected Integer doInBackground(File... params) {
            File mCacheDir = params[0];
            String[] tempList = mCacheDir.list();
            if (tempList.length != 0) {
                File temp = null;
                for (String name : tempList) {
                    temp = new File(mCacheDir, name);
                    temp.delete();
                }
                return 1;
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Integer i) {
            super.onPostExecute(i);
            if (i != null)
                Utility.showToast(EntryActivity.this, "缓存已清除", Toast.LENGTH_SHORT);
        }
    }
}
