package com.happy.samuelalva.bcykari.ui.activity;

import com.happy.samuelalva.bcykari.support.adapter.ImagePagerAdapter;
import com.happy.samuelalva.bcykari.support.http.PixivHttpClient;
import com.happy.samuelalva.bcykari.ui.activity.base.BaseImageActivity;

/**
 * Created by Samuel.Alva on 2015/6/15.
 */
public class PixivImageActivity extends BaseImageActivity {
    @Override
    protected ImagePagerAdapter getAdapter() {
        if (urls.size() == 1) {
            urls.set(0, urls.get(0).replace("c/1200x1200/img-master", "img-original").replace("_master1200", ""));
        }
        return new ImagePagerAdapter(this, urls, mCacheDir, ImagePagerAdapter.PIXIV_SOURCE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PixivHttpClient.cancel(this);
    }
}
