package com.happy.samuelalva.bcykari;

import android.app.Application;
import android.os.AsyncTask;
import android.os.Environment;

import com.happy.samuelalva.bcykari.support.Utility;

import java.io.File;

/**
 * Created by Samuel.Alva on 2015/6/10.
 */
public class BPicApplication extends Application {
    private static File mImageCacheDir, mImageSaveDir;

    public static File getImageCacheDir() {
        return mImageCacheDir;
    }

    public static File getImageSaveDir() {
        return mImageSaveDir;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        File file = getExternalCacheDir();
        if (file != null) {
            mImageCacheDir = new File(getExternalCacheDir().getPath() + "/image");
        } else {
            mImageCacheDir = new File(Environment.getExternalStorageDirectory().getPath() + "/Android/data/" + getPackageName() + "/cache/image");
        }
        if (!mImageCacheDir.exists()) {
            mImageCacheDir.mkdirs();
        }

        mImageSaveDir = new File(Environment.getExternalStorageDirectory().getPath() + "/BPic");
        if (!mImageSaveDir.exists()) {
            mImageSaveDir.mkdirs();
        }

        new DeleteTask().execute(mImageCacheDir);
    }

    private class DeleteTask extends AsyncTask<File, Void, Boolean> {
        @Override
        protected Boolean doInBackground(File... params) {
            return Utility.cleanCache();
        }

        @Override
        protected void onPostExecute(Boolean b) {
            super.onPostExecute(b);
            if (b)
                Utility.showToast(BPicApplication.this, "缓存已清除");
        }
    }
}
