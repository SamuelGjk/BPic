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
        for (int i = 0; i < urls.size(); i++) {
            urls.set(i, urls.get(i).replace("c/240x480/img-master", "img-original").replace("_master1200", ""));
        }
        return new ImagePagerAdapter(this, urls, ImagePagerAdapter.PIXIV_SOURCE);
    }

    @Override
    protected void onDestroy() {
        PixivHttpClient.cancel(this);
        super.onDestroy();
    }
}
