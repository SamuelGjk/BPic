package com.happy.samuelalva.bcykari.support.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.happy.samuelalva.bcykari.R;
import com.happy.samuelalva.bcykari.support.Utility;
import com.happy.samuelalva.bcykari.support.http.BcyHttpClient;
import com.happy.samuelalva.bcykari.support.http.PixivHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;

import org.apache.http.Header;

import java.io.File;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Samuel.Alva on 2015/4/16.
 */
public class ImagePagerAdapter extends PagerAdapter implements View.OnClickListener {
    private final String TAG = "ImagePagerAdapter";
    public static final int BCY_SOURCE = 0, PIXIV_SOURCE = 1;

    private static final long MIN_FILE_SIZE = 1024 * 10;

    private Context context;
    private List<String> urls;
    private List<View> mViews = new ArrayList<>();
    private LayoutInflater mInflater;
    private File mCacheDir;
    private int dataSource;

    public ImagePagerAdapter(Context context, List<String> urls, File mCacheDir, int dataSource) {
        this.context = context;
        this.urls = urls;
        this.mCacheDir = mCacheDir;
        this.dataSource = dataSource;

        mInflater = LayoutInflater.from(context);

        for (int i = 0; i < urls.size(); i++) {
            mViews.add(null);
        }
    }

    @Override
    public int getCount() {
        return urls.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, int position) {
        View v = mViews.get(position);
        if (v != null) {
            container.addView(v);
            return v;
        } else {
            v = mInflater.inflate(R.layout.image_layout, null);
            v.setOnClickListener(this);
            container.addView(v);
            mViews.set(position, v);

            final SubsamplingScaleImageView iv = (SubsamplingScaleImageView) v.findViewById(R.id.scale_image_view);
            iv.setMaxScale(3.0f);
            iv.setDoubleTapZoomScale(2.0f);
            iv.setOnClickListener(this);

            final NumberProgressBar npb = (NumberProgressBar) v.findViewById(R.id.number_progress_bar);

            final String url = urls.get(position);
            final String cacheName = Utility.getCacheName(url);

            final File file = new File(mCacheDir, cacheName);
            final File tempFile = new File(mCacheDir, cacheName + ".temp");

            if (file.exists()) {
                npb.setProgress(100);
                iv.setImage(ImageSource.bitmap(Utility.createImageThumbnail(file.getPath(), context)));
                iv.setVisibility(View.VISIBLE);
                npb.setVisibility(View.GONE);
            } else {
                if (Utility.readNetworkState(context)) {
                    FileAsyncHttpResponseHandler handler = new FileAsyncHttpResponseHandler(tempFile) {
                        @Override
                        public void onProgress(int bytesWritten, int totalSize) {
                            super.onProgress(bytesWritten, totalSize);
                            npb.setProgress(bytesWritten * 100 / totalSize);
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, File tempFile) {
                            if (tempFile.length() < MIN_FILE_SIZE) {
                                Utility.showToast(context, "因为各种原因数据出错了=-=", Toast.LENGTH_SHORT);
                            } else {
                                tempFile.renameTo(file);
                                iv.setImage(ImageSource.bitmap(Utility.createImageThumbnail(file.getPath(), context)));
                                iv.setVisibility(View.VISIBLE);
                                npb.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable error, File tempFile) {
                            Log.i(TAG, statusCode + "");
                            if (statusCode == HttpURLConnection.HTTP_NOT_FOUND && url.endsWith(".jpg")) {
                                urls.set(0, url.replace("jpg", "png"));
                                notifyDataSetChanged();
                            } else {
                                Utility.showToastForLoadFailure(context);
                            }
                        }

                        @Override
                        public void onCancel() {
                            super.onCancel();
                            tempFile.delete();
                        }
                    };
                    if (dataSource == BCY_SOURCE) {
                        BcyHttpClient.get(context, url, handler);
                    } else {
                        PixivHttpClient.get(context, url, handler);
                    }
                } else {
                    Utility.showToastForNoNetwork(context);
                }
            }
            return v;
        }
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public void notifyDataSetChanged() {
        mViews.set(0, null);
        super.notifyDataSetChanged();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public void onClick(View v) {
        ((Activity) context).finish();
    }
}
