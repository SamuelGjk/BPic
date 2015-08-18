package com.happy.samuelalva.bcykari;

import android.app.Application;
import android.net.Uri;
import android.os.Environment;

import com.happy.samuelalva.bcykari.support.Constants;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.UrlConnectionDownloader;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;

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
    }
}
