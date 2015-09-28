package com.happy.samuelalva.bcykari.support.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.happy.samuelalva.bcykari.BPicApplication;
import com.happy.samuelalva.bcykari.R;
import com.happy.samuelalva.bcykari.receiver.ConnectivityReceiver;
import com.happy.samuelalva.bcykari.support.Utility;
import com.happy.samuelalva.bcykari.support.http.BPicHttpClient;
import com.happy.samuelalva.bcykari.support.image.BitmapCache;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RangeFileAsyncHttpResponseHandler;

import java.io.File;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Samuel.Alva on 2015/4/16.
 */
public class ImagePagerAdapter extends PagerAdapter implements View.OnClickListener {
    private static final long MIN_FILE_SIZE = 1024 * 10;

    private Context context;
    private List<String> urls;
    private List<View> mViews = new ArrayList<>();
    private LayoutInflater mInflater;
    private File mCacheDir;
    private BitmapCache bitmapCache;
    private Header[] headers;


    public ImagePagerAdapter(Context context, List<String> urls, Header[] headers) {
        this.context = context;
        this.urls = urls;
        this.headers = headers;

        mCacheDir = BPicApplication.getImageCacheDir();
        mInflater = LayoutInflater.from(context);
        bitmapCache = BitmapCache.getInstance();

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
    public Object instantiateItem(ViewGroup container, final int position) {
        View v = mViews.get(position);
        if (v != null) {
            container.addView(v);
            return v;
        } else {
            v = mInflater.inflate(R.layout.image_layout, null);
            container.addView(v);
            mViews.set(position, v);

            View layoutImage = v.findViewById(R.id.layout_image);
            layoutImage.setOnClickListener(this);

            final View refreshBtn = v.findViewById(R.id.btn_refresh);

            final SubsamplingScaleImageView iv = (SubsamplingScaleImageView) v.findViewById(R.id.scale_image_view);
            iv.setMaxScale(3.0f);
            iv.setDoubleTapZoomScale(2.0f);
            iv.setOnClickListener(this);

            final RoundCornerProgressBar mProgressBar = (RoundCornerProgressBar) v.findViewById(R.id.round_corner_progress_bar);

            final Animation animation = AnimationUtils.loadAnimation(context, R.anim.fade_in);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    mProgressBar.setVisibility(View.GONE);
                    iv.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    iv.clearAnimation();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            final String url = urls.get(position);
            final String cacheName = Utility.getCacheName(url);

            Bitmap bitmap = bitmapCache.getBitmap(cacheName);
            if (bitmap != null) {
                iv.setImage(ImageSource.bitmap(bitmap));
                iv.startAnimation(animation);
            } else {
                final File cacheFile = new File(mCacheDir, cacheName);
                if (cacheFile.exists()) {
                    bitmap = Utility.createPreviewImage(cacheFile.getPath(), context);
                    iv.setImage(ImageSource.bitmap(bitmap));
                    iv.startAnimation(animation);
                    bitmapCache.putBitmap(cacheName, bitmap);
                } else {
                    File tempFile = new File(mCacheDir, cacheName + ".temp");
                    final RangeFileAsyncHttpResponseHandler handler = new RangeFileAsyncHttpResponseHandler(tempFile) {
                        @Override
                        public void onProgress(long bytesWritten, long totalSize) {
                            super.onProgress(bytesWritten, totalSize);
                            mProgressBar.setProgress((float) (bytesWritten * 100 / totalSize));
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, File file) {
                            if (file.length() < MIN_FILE_SIZE) {
                                Utility.showToast(context, "因为各种原因出错了=-=");
                            } else {
                                file.renameTo(cacheFile);
                                Bitmap bitmap = Utility.createPreviewImage(cacheFile.getPath(), context);
                                iv.setImage(ImageSource.bitmap(bitmap));
                                iv.startAnimation(animation);
                                bitmapCache.putBitmap(cacheName, bitmap);
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                            if (statusCode == HttpURLConnection.HTTP_NOT_FOUND && url.endsWith(".jpg")) {
                                file.delete();
                                doRequest(url.replace("jpg", "png"), this);
                            } else {
                                refreshBtn.setVisibility(View.VISIBLE);
                                mProgressBar.setVisibility(View.GONE);
                            }
                        }
                    };

//                    final FileAsyncHttpResponseHandler handler = new FileAsyncHttpResponseHandler(tempFile) {
//                        @Override
//                        public void onProgress(long bytesWritten, long totalSize) {
//                            super.onProgress(bytesWritten, totalSize);
//                            mProgressBar.setProgress((float) (bytesWritten * 100 / totalSize));
//                        }
//
//                        @Override
//                        public void onSuccess(int statusCode, Header[] headers, File tempFile) {
//                            if (tempFile.length() < MIN_FILE_SIZE) {
//                                Utility.showToast(context, "因为各种原因出错了=-=");
//                            } else {
//                                tempFile.renameTo(cacheFile);
//                                Bitmap bitmap = Utility.createPreviewImage(cacheFile.getPath(), context);
//                                iv.setImage(ImageSource.bitmap(bitmap));
//                                iv.startAnimation(animation);
//                                bitmapCache.putBitmap(cacheName, bitmap);
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(int statusCode, Header[] headers, Throwable error, File tempFile) {
//                            if (statusCode == HttpURLConnection.HTTP_NOT_FOUND && url.endsWith(".jpg")) {
//                                tempFile.delete();
//                                doRequest(url.replace("jpg", "png"), this);
//                            } else {
//                                refreshBtn.setVisibility(View.VISIBLE);
//                                mProgressBar.setVisibility(View.GONE);
//                            }
//                        }
//                    };

                    refreshBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            refreshBtn.setVisibility(View.GONE);
                            mProgressBar.setProgress(0.0f);
                            mProgressBar.setVisibility(View.VISIBLE);
                            doRequest(url, handler);
                        }
                    });

                    if (ConnectivityReceiver.isConnected) {
                        doRequest(url, handler);
                    } else {
                        Utility.showToast(context, context.getString(R.string.no_network));
                        refreshBtn.setVisibility(View.VISIBLE);
                        mProgressBar.setVisibility(View.GONE);
                    }
                }
            }
            return v;
        }
    }

    private void doRequest(String url, AsyncHttpResponseHandler handler) {
        BPicHttpClient.get(context, url, headers, handler);
    }

    public void stopDownload() {
        BPicHttpClient.cancel(context);
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
