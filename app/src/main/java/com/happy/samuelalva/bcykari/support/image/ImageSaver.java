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

package com.happy.samuelalva.bcykari.support.image;

import android.content.Context;
import android.os.AsyncTask;

import com.happy.samuelalva.bcykari.BPicApplication;
import com.happy.samuelalva.bcykari.support.Utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Samuel.Alva on 2015/4/19.
 */
public class ImageSaver {
    private static ImageSaver saver;
    private File mDir;
    private Context context;

    public static ImageSaver getInstance(Context context) {
        if (saver == null) {
            saver = new ImageSaver(context);
        }
        return saver;
    }

    private ImageSaver(Context context) {
        this.context = context;
        mDir = BPicApplication.getImageSaveDir();
    }

    public void save(File file, String name) {
        new SaveImageTask().execute(new Object[]{file, name});
    }

    private class SaveImageTask extends AsyncTask<Object, Void, String> {

        @Override
        protected String doInBackground(Object... params) {
            String fileName = params[1].toString();
            File f1 = new File(mDir, fileName);
            File f2 = null;
            if (fileName.contains("jpg")) f2 = new File(mDir, fileName.replace("jpg", "png"));
            if (f1.exists() || (f2 != null && f2.exists())) {
                return "图片已存在";
            }
            FileInputStream fis = null;
            FileOutputStream fos = null;
            try {
                fis = new FileInputStream((File) params[0]);
                fos = new FileOutputStream(f1);

                byte[] buf = new byte[1024];
                int len = 0;

                while ((len = fis.read(buf)) != -1) {
                    fos.write(buf, 0, len);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fis != null) {
                        fis.close();
                    }
                    if (fos != null) {
                        fos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return "图片已保存到" + mDir.getPath();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Utility.showToast(context, result);
        }
    }
}
