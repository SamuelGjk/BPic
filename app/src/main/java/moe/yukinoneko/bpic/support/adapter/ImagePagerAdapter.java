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

package moe.yukinoneko.bpic.support.adapter;

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
import com.loopj.android.http.FileAsyncHttpResponseHandler;

import java.io.File;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import moe.yukinoneko.bpic.BPicApplication;
import moe.yukinoneko.bpic.R;
import moe.yukinoneko.bpic.receiver.ConnectivityReceiver;
import moe.yukinoneko.bpic.support.Utility;
import moe.yukinoneko.bpic.support.http.BPicHttpClient;
import moe.yukinoneko.bpic.support.image.BitmapCache;
import moe.yukinoneko.bpic.ui.activity.base.BaseGalleryActivity;

/**
 * Created by Samuel.Alva on 2015/4/16.
 */
public class ImagePagerAdapter extends PagerAdapter implements View.OnClickListener {
    private final long MIN_FILE_SIZE = 1024 * 10;

    private Context context;
    private List<String> urls;
    private List<View> mViews = new ArrayList<>();
    private LayoutInflater mInflater;
    private File mCacheDir;
    private BitmapCache bitmapCache;
    private Header[] headers;
    private boolean[] isDownloaded;

    public ImagePagerAdapter(Context context, List<String> urls, Header[] headers) {
        this.context = context;
        this.urls = urls;
        this.headers = headers;

        mCacheDir = BPicApplication.getImageCacheDir();
        mInflater = LayoutInflater.from(context);
        bitmapCache = BitmapCache.getInstance();
        isDownloaded = new boolean[urls.size()];

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

            refreshBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    refreshBtn.setVisibility(View.GONE);
                    mProgressBar.setProgress(0.0f);
                    mProgressBar.setVisibility(View.VISIBLE);
                    loadPicture(urls.get(position), position, mProgressBar, iv, refreshBtn, animation);
                }
            });

            loadPicture(urls.get(position), position, mProgressBar, iv, refreshBtn, animation);

            return v;
        }
    }

    private void updateImageView(SubsamplingScaleImageView imageView, Bitmap bitmap, Animation animation) {
        imageView.setImage(ImageSource.bitmap(bitmap));
        imageView.startAnimation(animation);
    }

    private void loadPicture(final String url, final int position, final RoundCornerProgressBar progressBar, final SubsamplingScaleImageView imageView, final View refreshBtn, final Animation animation) {
        if (!ConnectivityReceiver.isConnected) {
            Utility.showToast(context, R.string.no_network);
            refreshBtn.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            return;
        }

        final String cacheName = Utility.getCacheName(url);
        final File cacheFile = new File(mCacheDir, cacheName);
        isDownloaded[position] = cacheFile.exists();

        Bitmap bitmap = bitmapCache.getBitmap(cacheName);
        if (bitmap != null) {
            updateImageView(imageView, bitmap, animation);
        } else {
            if (isDownloaded[position]) {
                bitmap = Utility.createPreviewImage(cacheFile.getPath(), context);
                updateImageView(imageView, bitmap, animation);
                bitmapCache.putBitmap(cacheName, bitmap);
            } else {
                FileAsyncHttpResponseHandler handler = new FileAsyncHttpResponseHandler(cacheFile) {
                    @Override
                    public void onProgress(long bytesWritten, long totalSize) {
                        super.onProgress(bytesWritten, totalSize);
                        progressBar.setProgress((float) (bytesWritten * 100 / totalSize));
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, File cacheFile) {
                        if (cacheFile.length() < MIN_FILE_SIZE) {
                            Utility.showToast(context, "因为各种原因出错了=-=");
                        } else {
                            isDownloaded[position] = true;
                            Bitmap bitmap = Utility.createPreviewImage(cacheFile.getPath(), context);
                            updateImageView(imageView, bitmap, animation);
                            bitmapCache.putBitmap(cacheName, bitmap);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable error, File cacheFile) {
                        cacheFile.delete();
                        if (statusCode == HttpURLConnection.HTTP_NOT_FOUND && url.endsWith(".jpg")) {
                            String newUrl = url.replace("jpg", "png");
                            urls.set(position, newUrl);
                            loadPicture(newUrl, position, progressBar, imageView, refreshBtn, animation);
                        } else {
                            refreshBtn.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancel() {
                        super.onCancel();
                        cacheFile.delete();
                    }
                };
                BPicHttpClient.get(context, url, headers, handler);
            }
        }
    }

    public void stopDownload() {
        BPicHttpClient.cancel(context);
    }

    public boolean isDownloaded(int position) {
        return isDownloaded[position];
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public void onClick(View v) {
        ((BaseGalleryActivity) context).SystemUIConfig();
    }
}
