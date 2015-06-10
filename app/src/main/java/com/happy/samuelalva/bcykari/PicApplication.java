package com.happy.samuelalva.bcykari;

import android.app.Application;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.happy.samuelalva.bcykari.support.Utility;

import java.io.File;

/**
 * Created by Samuel.Alva on 2015/6/10.
 */
public class PicApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        File mCacheDir = new File(getExternalCacheDir().getPath() + "/image");
        if (!mCacheDir.exists()) {
            mCacheDir.mkdirs();
        }

        File mImageDir = new File(Environment.getExternalStorageDirectory().getPath() + "/BcyKari");
        if (!mImageDir.exists()) {
            mImageDir.mkdirs();
        }

        new DeleteTask().execute(mCacheDir);

        Fresco.initialize(this);
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
                Utility.showToast(PicApplication.this, "缓存已清除", Toast.LENGTH_SHORT);
        }
    }
}
