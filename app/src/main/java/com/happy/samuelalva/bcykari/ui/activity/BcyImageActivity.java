package com.happy.samuelalva.bcykari.ui.activity;

import com.happy.samuelalva.bcykari.support.adapter.ImagePagerAdapter;
import com.happy.samuelalva.bcykari.support.http.BcyHttpClient;
import com.happy.samuelalva.bcykari.ui.activity.base.BaseImageActivity;

/**
 * Created by Samuel.Alva on 2015/6/15.
 */
public class BcyImageActivity extends BaseImageActivity {
    @Override
    protected ImagePagerAdapter getAdapter() {
        return new ImagePagerAdapter(this, urls, mCacheDir, ImagePagerAdapter.BCY_SOURCE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BcyHttpClient.cancel(this);
    }
}
