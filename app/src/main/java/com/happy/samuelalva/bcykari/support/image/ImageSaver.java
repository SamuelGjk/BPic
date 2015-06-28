package com.happy.samuelalva.bcykari.support.image;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;

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

    public static ImageSaver getInstance() {
        if (saver == null) {
            saver = new ImageSaver();
        }
        return saver;
    }

    private ImageSaver() {
        mDir = new File(Environment.getExternalStorageDirectory().getPath() + "/BcyKari");
    }

    public void save(Context context, File file, String name) {
        new SaveImageTask().execute(new Object[]{context, file, name});
    }

    private class SaveImageTask extends AsyncTask<Object, Void, Object[]> {

        @Override
        protected Object[] doInBackground(Object... params) {
            File f = new File(mDir, params[2].toString());
            if (f.exists()) {
                return new Object[]{params[0], "图片已存在"};
            }
            FileInputStream fis = null;
            FileOutputStream fos = null;
            try {
                fis = new FileInputStream((File) params[1]);
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
            return new Object[]{params[0], "图片已保存到" + mDir.getPath()};
        }

        @Override
        protected void onPostExecute(Object[] objects) {
            super.onPostExecute(objects);
            Utility.showToast((Context) objects[0], objects[1].toString());
        }
    }
}
