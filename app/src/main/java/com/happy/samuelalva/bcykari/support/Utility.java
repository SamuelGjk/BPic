package com.happy.samuelalva.bcykari.support;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

import com.happy.samuelalva.bcykari.R;

/**
 * Created by Samuel.Alva on 2015/5/4.
 */
public class Utility {

    /*
    public static float dp2px(Context context, float dp) {
        return context.getResources().getDisplayMetrics().density * dp + 0.5f;
    }

    public static int getActionBarHeight(Context context) {
        TypedValue v = new TypedValue();

        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, v, true)) {
            return TypedValue.complexToDimensionPixelSize(v.data, context.getResources().getDisplayMetrics());
        } else {
            return 0;
        }
    }
    */

    public static int getStatusBarHeight(Context context) {
        Resources res = context.getResources();
        int result = 0;
        int resourceId = res.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = res.getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static String getCacheName(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void showToastForNoNetwork(Context context) {
        Toast.makeText(context, context.getResources().getString(R.string.no_network), Toast.LENGTH_SHORT).show();
    }

    public static void showToastForLoadFailure(Context context) {
        Toast.makeText(context, context.getResources().getString(R.string.load_failed), Toast.LENGTH_SHORT).show();
    }

    public static void showToastForMobileData(Context context) {
        Toast.makeText(context, context.getResources().getString(R.string.mobile_data), Toast.LENGTH_SHORT).show();
    }

    private static int getMaxNumOfPixels(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        return width * height;
    }

    public static Bitmap createImageThumbnail(String path, Context context) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, opts);

        opts.inSampleSize = computeSampleSize(opts, -1, getMaxNumOfPixels(context));
        opts.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, opts);
    }

    private static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }
        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;
        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));
        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }
        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

}
