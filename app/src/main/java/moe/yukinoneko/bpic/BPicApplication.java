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

package moe.yukinoneko.bpic;

import android.app.Application;
import android.os.Environment;

import java.io.File;

import moe.yukinoneko.bpic.support.CrashHandler;

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

        CrashHandler.addCrashHandler(getApplicationContext());

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
