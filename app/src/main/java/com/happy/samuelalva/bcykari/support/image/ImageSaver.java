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
            File f = new File(mDir, params[1].toString());
            if (f.exists()) {
                return "图片已存在";
            }
            FileInputStream fis = null;
            FileOutputStream fos = null;
            try {
                fis = new FileInputStream((File) params[0]);
                fos = new FileOutputStream(f);

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
