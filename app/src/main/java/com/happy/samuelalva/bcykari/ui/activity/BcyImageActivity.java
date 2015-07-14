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
        for (int i = 0; i < urls.size(); i++) {
            urls.set(i, urls.get(i).replace("/2X3", ""));
        }
        return new ImagePagerAdapter(this, urls, ImagePagerAdapter.BCY_SOURCE);
    }

    @Override
    protected void onDestroy() {
        BcyHttpClient.cancel(this);
        super.onDestroy();
    }
}
